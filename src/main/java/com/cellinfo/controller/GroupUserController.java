package com.cellinfo.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cellinfo.annotation.ServiceLog;
import com.cellinfo.controller.entity.FieldParameter;
import com.cellinfo.controller.entity.FilterParameter;
import com.cellinfo.controller.entity.KernelParameter;
import com.cellinfo.controller.entity.RequestParameter;
import com.cellinfo.controller.entity.SubtypeParameter;
import com.cellinfo.controller.entity.TaskParameter;
import com.cellinfo.entity.Result;
import com.cellinfo.entity.TlGammaKernel;
import com.cellinfo.entity.TlGammaKernelAttr;
import com.cellinfo.entity.TlGammaKernelExt;
import com.cellinfo.entity.TlGammaKernelFilter;
import com.cellinfo.entity.TlGammaTask;
import com.cellinfo.entity.TlGammaUser;
import com.cellinfo.security.UserInfo;
import com.cellinfo.service.SysGroupService;
import com.cellinfo.service.SysKernelExtService;
import com.cellinfo.service.SysKernelService;
import com.cellinfo.service.SysTaskService;
import com.cellinfo.service.UtilService;
import com.cellinfo.utils.ResultUtil;
import com.cellinfo.utils.ReturnDesc;

/**
 *  数据分类
 *  任务管理(创建任务，用户任务列表，查询任务)
 * 组织内部的任务管理
 * 任务创建 
 * 任务列表
 * 数据分类（数据按空间过虑，按字段过滤，生成子类）
 * @author zhangjian
 */
@ServiceLog(moduleName = "组织管理接口")
@PreAuthorize("hasRole('ROLE_GROUP_ADMIN') OR hasRole('ROLE_USER')")  
@RestController
@RequestMapping("/service/user")
public class GroupUserController {
	private final static Logger logger = LoggerFactory.getLogger(GroupUserController.class);
	
	@Autowired
	private UtilService utilService;
	
	@Autowired
	private SysTaskService sysTaskService ;
	
	@Autowired
	private SysKernelService sysKernelService;
	
	@Autowired
	private SysGroupService sysGroupService;
	
	@Autowired
	private SysKernelExtService sysKernelExtService;
	
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");   
	
	@PostMapping(value = "/tasks")
	public Result<List<TaskParameter>> userTasks(HttpServletRequest request,@RequestBody RequestParameter para, BindingResult bindingResult) {
		UserInfo cUser = this.utilService.getCurrentUser(request);
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		int pageNumber = para.getPage();
		int pageSize = para.getPageSize();
		Sort sort = null; 
		String sortField ="userName";
		if (para.getSortDirection()!=null && para.getSortDirection().equalsIgnoreCase("ASC")) {
			sort = new Sort(Direction.ASC, sortField);
		} else {
			sort = new Sort(Direction.DESC, sortField);
		}
		PageRequest pageInfo = new PageRequest(pageNumber, pageSize, sort);
		Page<TlGammaTask> tasklist = this.sysTaskService.getByUserName(cUser.getUserName(),pageInfo);
		List<TaskParameter> rtasks  = new LinkedList<TaskParameter>();
		for(TlGammaTask task : tasklist)
		{
			TaskParameter tpara = new TaskParameter();
			tpara.setEndDate(df.format(task.getTaskTimeend()));
			tpara.setStartDate(df.format(task.getTaskTimestart()));
			tpara.setTaskGuid(task.getTaskGuid());
			tpara.setTaskName(task.getTaskName());
			rtasks.add(tpara);
		}
		return ResultUtil.success(rtasks);
	}
	
	@PostMapping(value = "/task/query")
	public Result<TaskParameter> queryTaskInfo(HttpServletRequest request,@RequestBody String taskGuid, BindingResult bindingResult) {
		UserInfo cUser = this.utilService.getCurrentUser(request);
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		TlGammaTask realTask = this.sysTaskService.getByGuid(taskGuid);
		TaskParameter tpara = new TaskParameter();

		tpara.setEndDate(df.format(realTask.getTaskTimeend()));
		tpara.setStartDate(df.format(realTask.getTaskTimestart()));
		tpara.setTaskGuid(realTask.getTaskGuid());
		tpara.setTaskName(realTask.getTaskName());
		
		return ResultUtil.success(tpara);
	}

	@PostMapping(value = "/task/initdata")
	public Result<Map<String,Object>> initTaskData(HttpServletRequest request) {
		Map<String,Object> resList = new HashMap<String ,Object>();
		
		UserInfo cUser = this.utilService.getCurrentUser(request);
		
		List<TlGammaUser> userList = this.sysGroupService.getUserByGroup(cUser.getGroupGuid());
		
		List<TlGammaKernel> kernelClassList = this.sysGroupService.getKernelClassByGroup(cUser.getGroupGuid());
		
		List<Map<String ,String>> userData = new LinkedList<Map<String,String>>();
		for(TlGammaUser user : userList) {
			Map<String ,String > uMap = new HashMap<String,String>();
			uMap.put("userName", user.getUserName());
			uMap.put("userGuid",user.getUserGuid());
			userData.add(uMap);
		}
		
		List<Map<String ,Object>> kernelData = new LinkedList<Map<String,Object>>();
		for(TlGammaKernel kernel : kernelClassList) {
			Map<String ,Object > kMap = new HashMap<String,Object>();
			kMap.put("className",kernel.getKernelClassname());
			kMap.put("classId",kernel.getKernelClassid());
			kMap.put("extList", this.sysKernelExtService.getKernelExtList(kernel.getKernelClassid(), cUser.getUserName()));
			kernelData.add(kMap);
		}
		resList.put("userList", userData);
		resList.put("kernelList", kernelData);

		return ResultUtil.success(resList);
	}

	/**
	 * 创建任务
	 * 保存任务基本信息
	 * 任务对应的核心对象类别必选
	 */
	@Transactional
	@PostMapping(value = "/task/save")
	public Result<Map<String,Object>> createTask(HttpServletRequest request,
			@RequestBody @Valid TaskParameter taskParam, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		Map<String,Object> resList = new HashMap<String ,Object>();
		UserInfo cUser = this.utilService.getCurrentUser(request);
		try {
			//任务基本信息
			TlGammaTask task = new TlGammaTask();
			String taskGuid = UUID.randomUUID().toString();
			task.setTaskGuid(taskGuid);
			task.setTaskName(taskParam.getTaskName());
			task.setTaskTimestart( Timestamp.valueOf(taskParam.getStartDate()));
			task.setTaskTimeend(Timestamp.valueOf(taskParam.getEndDate()));
			task.setUserName(cUser.getUserName());
			if(taskParam.getClassid() == null ||taskParam.getClassid().length()<2)
				return ResultUtil.error(400, ReturnDesc.KERNEL_CLASS_SHOULD_NOT_NULL);
			task.setKernelClassid(taskParam.getClassid());
			task.setKernelAdd(taskParam.getKernelAdd());
			if(taskParam.getBusPassword()!=null)
				task.setBusinessPassword(taskParam.getBusPassword());
			
			//根据核心对象类别和核心对象的子类，将核心对象关联到任务
			addKernelToTask(taskGuid,taskParam.getClassid(),taskParam.getExtGuid());
			
			TlGammaTask tmpTask = this.sysTaskService.addTask(task);
			resList.put("taskGuid", tmpTask.getTaskGuid());
			resList.put("taskName", tmpTask.getTaskName());
		} catch(Exception e){
			throw new RuntimeException("error");
		}

		return ResultUtil.success(resList);
	}
	
	private void addKernelToTask(String taskGuid, String kernelClassid, String refExtGuid) {
		// TODO Auto-generated method stub
		if(refExtGuid != null) {
			this.sysKernelService.transferTaskKernelList(taskGuid, kernelClassid,refExtGuid);
		}
		else
		{
			this.sysKernelService.transferTaskKernelList(taskGuid, kernelClassid);
		}
	}

	@PostMapping(value = "/task/update")
	public Result<TlGammaTask> updateTask(@RequestBody @Valid TaskParameter taskParam,BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		Map<String,Object> resList = new HashMap<String ,Object>();
		try {
			if(taskParam.getTaskGuid()!= null)
			{
				TlGammaTask task = this.sysTaskService.getByGuid(taskParam.getTaskGuid());
				if(taskParam.getStartDate()!=null)
					task.setTaskTimestart( Timestamp.valueOf(taskParam.getStartDate()));
				if(taskParam.getEndDate()!=null)
					task.setTaskTimeend(Timestamp.valueOf(taskParam.getEndDate()));
				if(taskParam.getBusPassword()!=null)
					task.setBusinessPassword(taskParam.getBusPassword());
				TlGammaTask tmpTask = this.sysTaskService.updateTask(task);
				resList.put("taskGuid", tmpTask.getTaskGuid());
				resList.put("taskName", tmpTask.getTaskName());
			}
			//任务基本信息
			
			
		} catch(Exception e){
			throw new RuntimeException("error");
		}
		return ResultUtil.success(resList);
	}
	
	@PostMapping(value = "/taskuser/add")
	public Result<TlGammaTask> addTaskUser(@RequestBody @Valid TaskParameter taskParam,BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		if(taskParam.getTaskGuid()!= null && taskParam.getField()!=null)
		{
			
		}
		
		return ResultUtil.success("");
	}
	@PostMapping(value = "/taskuser/delete")
	public Result<TlGammaTask> deleteTaskUser(@RequestBody @Valid TaskParameter taskParam,BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		if(taskParam.getTaskGuid()!= null && taskParam.getField()!=null)
		{
			
		}
		
		return ResultUtil.success("");
	}
	
	/**
	 * 添加字段需要当前任务事先确定核心对象类别
	 * 
	 * @param taskParam
	 * @param bindingResult
	 * @return
	 */
	@PostMapping(value = "/taskfield/add")
	public Result<TlGammaTask> addTaskField(@RequestBody @Valid TaskParameter taskParam,BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		if(taskParam.getTaskGuid()!= null && taskParam.getField()!=null)
		{
			
		}
		
		return ResultUtil.success("");
	}
	@PostMapping(value = "/taskfield/delete")
	public Result<TlGammaTask> deleteTaskField(@RequestBody @Valid TaskParameter taskParam,BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		if(taskParam.getTaskGuid()!= null && taskParam.getField()!=null)
		{
			
		}
		
		return ResultUtil.success("");
	}
	
	@PostMapping(value = "/closedtasks")
	public Result<Page<TlGammaTask>> groupList(@RequestBody RequestParameter para, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		logger.info("userList");
		int pageNumber = para.getPage();
		int pageSize = para.getPageSize();

		Sort sort = null;
		if (para.getSortDirection().equalsIgnoreCase("ASC")) {
			sort = new Sort(Direction.ASC, para.getSortField());
		} else {
			sort = new Sort(Direction.DESC, para.getSortField());
		}
		PageRequest pageInfo = new PageRequest(pageNumber, pageSize, sort);
		Page<TlGammaTask> mList = this.sysTaskService.getTaskList(pageInfo);
		return ResultUtil.success(mList);
	}
	
	@PostMapping(value = "/kernels")
	public Result<List<Map<String, Object>>> kernelList(HttpServletRequest request ,@RequestBody RequestParameter para, BindingResult bindingResult) {
		List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
		UserInfo cUser = this.utilService.getCurrentUser(request);
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		logger.info("kernels");
		int pageNumber = para.getPage();
		int pageSize = para.getPageSize();

		Sort sort = null;
		String sortField ="kernelClassname";
		
		if (para.getSortDirection()!=null && para.getSortDirection().equalsIgnoreCase("ASC")) {
			sort = new Sort(Direction.ASC, sortField);
		} else {
			sort = new Sort(Direction.DESC, sortField);
		}

		PageRequest pageInfo = new PageRequest(pageNumber, pageSize, sort);
		Page<TlGammaKernel> mList = this.sysKernelService.getGroupKernelList(cUser.getGroupGuid(),pageInfo);
		for (TlGammaKernel eachKernel : mList) { 
			Map<String, Object> tMap = new HashMap<String, Object>();
			tMap.put("classGuid", eachKernel.getKernelClassid());
			tMap.put("className", eachKernel.getKernelClassname());
			tMap.put("descInfo", eachKernel.getKernelClassdesc());
			list.add(tMap);
		}
		
		return ResultUtil.success(list);
	}
	
	///核对象信息
	@PostMapping(value = "/kernel/info")
	public Result<KernelParameter> getKernelInfo(@RequestBody String kernelClassid, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		logger.info("userList");
		KernelParameter  para = new KernelParameter();
		TlGammaKernel kernel = this.sysKernelService.getByKernelClassid(kernelClassid);
		para.setClassGuid(kernel.getKernelClassid());
		para.setClassName(kernel.getKernelClassname());
		para.setDescInfo(kernel.getKernelClassdesc());
		para.setGeomType(kernel.getGeomType());

		List<TlGammaKernelAttr>  attrList = this.sysKernelService.getKernelAttrList(kernelClassid);
		
		List<FieldParameter> fieldList = attrList.stream().map(item ->{
			FieldParameter field = new FieldParameter();
			field.setFieldGuid(item.getAttrGuid());
			field.setFieldName(item.getAttrName());
			field.setFieldType(item.getAttrType());
			return field;
		}).collect(Collectors.toList());
		
		para.setFieldList(fieldList);
		
		return ResultUtil.success(para);
	}
		
	
	@PostMapping(value = "/kernel/exttypes")
	public Result<Page<SubtypeParameter>> listKernelExttypes(HttpServletRequest request ,@RequestBody RequestParameter para, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}

		int pageNumber = para.getPage();
		int pageSize = para.getPageSize();

		Sort sort = null;
		String sortField ="extName";
		
		if (para.getSortDirection()!=null && para.getSortDirection().equalsIgnoreCase("ASC")) {
			sort = new Sort(Direction.ASC, sortField);
		} else {
			sort = new Sort(Direction.DESC, sortField);
		}

		PageRequest pageInfo = new PageRequest(pageNumber, pageSize, sort);
		
		UserInfo cUser = this.utilService.getCurrentUser(request);
		
		Page<SubtypeParameter> subList =  this.sysKernelExtService.getKernelExtList( para.getSkey(), cUser.getUserName(),pageInfo);
		
		return ResultUtil.success(subList);
	}
	///创建核心对象子类别
	///更新核心对象子类
	@PostMapping(value = "/kernel/exttype/save")
	public Result<TlGammaKernelExt> addKernelExttype(HttpServletRequest request ,@RequestBody SubtypeParameter para, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		UserInfo cUser = this.utilService.getCurrentUser(request);
		///保存子项
		///对条件进行检证保存
		TlGammaKernelExt tmp = new TlGammaKernelExt();
		if(para!=null && para.getFilterList()!=null && para.getFilterList().size()>0)
		{
			if(para.getExtGuid()!= null)
				this.sysKernelExtService.deleteKernelExt(para.getExtGuid());
			
			TlGammaKernelExt ext = new TlGammaKernelExt(); 
			List<TlGammaKernelFilter> filterList = new LinkedList<TlGammaKernelFilter>();
			ext.setExtGuid(UUID.randomUUID().toString());
			ext.setExtDesc(para.getExtDesc());
			ext.setExtName(ext.getExtName());
			ext.setKernelClassid(para.getKernelClassid());
			ext.setUserName(cUser.getUserName());
			
			for(FilterParameter filter : para.getFilterList())
			{
				if(filter.getAttrGuid()!= null)
				{
					TlGammaKernelAttr kernelAttr = this.sysKernelExtService.getKernelAttr(filter.getAttrGuid());
					TlGammaKernelFilter nFilter = new TlGammaKernelFilter();
					nFilter.setFilterGuid(UUID.randomUUID().toString());
					nFilter.setExtGuid(ext.getExtGuid());
					nFilter.setAttrGuid(filter.getAttrGuid());
					nFilter.setFilterType(filter.getType());
					nFilter.setMaxValue(filter.getMaxValue());
					nFilter.setMinValue(filter.getMinValue());
					nFilter.setAttrField(kernelAttr.getAttrField());
					nFilter.setAttrType(kernelAttr.getAttrType());
					filterList.add(nFilter);
				}
			}
			tmp = this.sysKernelExtService.saveKernelExt(ext, filterList);
		}

		return ResultUtil.success(tmp);
	}
	
	@PostMapping(value = "/kernel/exttype/delete")
	public Result<TlGammaKernelExt> kernelExttype(HttpServletRequest request ,@RequestBody String extGuid, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}

		this.sysKernelExtService.deleteKernelExt(extGuid);
		
		return ResultUtil.success(ReturnDesc.EXECUTION_SUCCESS);
	}
}
