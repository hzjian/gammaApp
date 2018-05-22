package com.cellinfo.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.geotools.geojson.geom.GeometryJSON;
import org.json.simple.JSONValue;
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

import com.cellinfo.annotation.OperLog;
import com.cellinfo.annotation.ServiceLog;
import com.cellinfo.controller.entity.ExtTypeParameter;
import com.cellinfo.controller.entity.FieldParameter;
import com.cellinfo.controller.entity.FilterParameter;
import com.cellinfo.controller.entity.KernelParameter;
import com.cellinfo.controller.entity.RequestParameter;
import com.cellinfo.controller.entity.TaskParameter;
import com.cellinfo.entity.Result;
import com.cellinfo.entity.TlGammaKernel;
import com.cellinfo.entity.TlGammaKernelAttr;
import com.cellinfo.entity.TlGammaKernelExt;
import com.cellinfo.entity.TlGammaKernelFilter;
import com.cellinfo.entity.TlGammaKernelGeoFilter;
import com.cellinfo.entity.TlGammaTask;
import com.cellinfo.entity.TlGammaTaskAttr;
import com.cellinfo.entity.TlGammaTaskExt;
import com.cellinfo.entity.TlGammaTaskUser;
import com.cellinfo.entity.TlGammaUser;
import com.cellinfo.entity.ViewTaskAttr;
import com.cellinfo.entity.ViewTaskUser;
import com.cellinfo.entity.ViewTaskUserPK;
import com.cellinfo.security.UserInfo;
import com.cellinfo.service.SysGroupService;
import com.cellinfo.service.SysKernelExtService;
import com.cellinfo.service.SysKernelService;
import com.cellinfo.service.SysTaskService;
import com.cellinfo.service.UtilService;
import com.cellinfo.utils.FuncDesc;
import com.cellinfo.utils.OperDesc;
import com.cellinfo.utils.ResultUtil;
import com.cellinfo.utils.ReturnDesc;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

/**
 *  数据分类
 *  任务管理(创建任务，用户任务列表，查询任务)
 * 组织内部的任务管理
 * 任务创建 
 * 任务列表
 * 数据分类（数据按空间过虑，按字段过滤，生成子类）
 * @author zhangjian
 */
@ServiceLog(moduleName = "组织用户功能模块")
@PreAuthorize("hasRole('ROLE_GROUP_ADMIN') OR hasRole('ROLE_USER')")  
@RestController
@RequestMapping("/service/user")
public class SysGroupUserController {
	private final static Logger logger = LoggerFactory.getLogger(SysGroupUserController.class);
	
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
	
	@OperLog(funcName = FuncDesc.GROUP_USER_TASK_QUERY, methodName = OperDesc.QUERY)
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
		Optional<TlGammaTask> realTask = this.sysTaskService.getByGuid(taskGuid);
		TaskParameter tpara = new TaskParameter();
		if(realTask.isPresent())
		{
			tpara.setEndDate(df.format(realTask.get().getTaskTimeend()));
			tpara.setStartDate(df.format(realTask.get().getTaskTimestart()));
			tpara.setTaskGuid(realTask.get().getTaskGuid());
			tpara.setTaskName(realTask.get().getTaskName());
		}
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
	//@Transactional
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
			if(taskParam.getClassId() == null ||taskParam.getClassId().length()<2)
				return ResultUtil.error(400, ReturnDesc.KERNEL_CLASS_SHOULD_NOT_NULL);
			task.setKernelClassid(taskParam.getClassId());
			task.setKernelAdd(taskParam.getKernelAdd());
			if(taskParam.getBusPassword()!=null)
				task.setBusinessPassword(taskParam.getBusPassword());
			
			//根据核心对象类别和核心对象的子类，将核心对象关联到任务
			addKernelToTask(taskGuid,taskParam.getClassId(),taskParam.getExtGuid());
			
			//将任务关联的核心对象及标签保存到数据库中
			TlGammaTaskExt taskExt = new TlGammaTaskExt();
			taskExt.setExtGuid(taskParam.getExtGuid());
			taskExt.setExtName(taskParam.getExtName());
			taskExt.setKernelClassid(taskParam.getClassId());
			taskExt.setKernelClassname(taskParam.getClassName());
			taskExt.setTaskGuid(taskGuid);
			taskExt.setKernelGrade(1);
			this.sysTaskService.saveTaskExt(taskExt);
			
			TlGammaTask tmpTask = this.sysTaskService.addTask(task);
			resList.put("taskGuid", tmpTask.getTaskGuid());
			resList.put("taskName", tmpTask.getTaskName());
		} catch(Exception e){
			throw new RuntimeException("error");
		}

		return ResultUtil.success(resList);
	}
	
	private void addKernelToTask(String taskGuid, String kernelClassid, String extGuid) {
		// TODO Auto-generated method stub
		if(extGuid != null) {
			this.sysKernelService.transferTaskKernelList(taskGuid, kernelClassid,extGuid,1);
		}
		else
		{
			this.sysKernelService.transferTaskKernelList(taskGuid, kernelClassid,1);
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
				Optional<TlGammaTask> taskOptional = this.sysTaskService.getByGuid(taskParam.getTaskGuid());
				if(taskOptional.isPresent())
				{
					TlGammaTask task = taskOptional.get();
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
			}
			//任务基本信息
			
			
		} catch(Exception e){
			throw new RuntimeException("error");
		}
		return ResultUtil.success(resList);
	}
	
	@PostMapping(value = "/taskuser/add")
	public Result<Map<String,Object>> addTaskUser(@RequestBody @Valid TaskParameter taskParam,BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		Map<String,Object> resList = new HashMap<String ,Object>();
		if(taskParam.getTaskGuid()!= null && taskParam.getUser()!=null && taskParam.getUser().getUserName()!=null)
		{
			TlGammaTaskUser taskUser =new TlGammaTaskUser();
			taskUser.setTaskGuid(taskParam.getTaskGuid());
			taskUser.setUserName(taskParam.getUser().getUserName());
			
			TlGammaTaskUser result = this.sysTaskService.saveTaskUser(taskUser);
			resList.put("taskGuid", result.getTaskGuid());
			resList.put("userName", result.getUserName());
		}
		
		return ResultUtil.success(resList);
	}
	@PostMapping(value = "/taskuser/delete")
	public Result<TlGammaTask> deleteTaskUser(@RequestBody @Valid TaskParameter taskParam,BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		if(taskParam.getTaskGuid()!= null && taskParam.getUser()!=null && taskParam.getUser().getUserName()!=null)
		{
			ViewTaskUserPK taskUserPk = new ViewTaskUserPK();
			taskUserPk.setTaskGuid(taskParam.getTaskGuid());
			taskUserPk.setUserName(taskParam.getUser().getUserName());
			this.sysTaskService.deleteTaskUser(taskParam.getTaskGuid(),taskParam.getUser().getUserName() );
		}
		
		
		return ResultUtil.success(ReturnDesc.EXECUTION_SUCCESS);
	}
	
	@PostMapping(value = "/taskuser/list")
	public Result<List<Map<String,Object>>> taskUserList(@RequestBody @Valid RequestParameter para,BindingResult bindingResult) {
		
		List<Map<String, Object>> taskUserList =  new LinkedList<Map<String, Object>>();
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		int pageNumber = para.getPage();
		int pageSize = para.getPageSize();

		Sort sort = null;
		if (para.getSortDirection().equalsIgnoreCase("ASC")) {
			sort = new Sort(Direction.ASC, para.getSortField());
		} else {
			sort = new Sort(Direction.DESC, para.getSortField());
		}
		PageRequest pageInfo = new PageRequest(pageNumber, pageSize, sort);
		
		if(para.getSkey()!= null && para.getSkey().length()>0)
		{
			Page<ViewTaskUser> tmpList = this.sysTaskService.getTaskUserList(para.getSkey(), pageInfo);
			for(ViewTaskUser tuser: tmpList.getContent())
			{
				Map<String,Object> resList = new HashMap<String ,Object>();
				resList.put("taskGuid", tuser.getId().getTaskGuid());
				resList.put("userName", tuser.getId().getUserName());
				resList.put("userCnname",tuser.getUserCnname());
				resList.put("userEmail",tuser.getUserEmail());
				taskUserList.add(resList);
			}
		}
		
		return ResultUtil.success(taskUserList);
	}
	
	/**
	 * @param taskParam
	 * @param bindingResult
	 * @return
	 */
	@PostMapping(value = "/taskfield/list")
	public Result<List<Map<String,Object>>> taskFieldList(@RequestBody @Valid RequestParameter para,BindingResult bindingResult) {
		
		List<Map<String, Object>> taskUserList =  new LinkedList<Map<String, Object>>();
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		int pageNumber = para.getPage();
		int pageSize = para.getPageSize();

		Sort sort = null;
		if (para.getSortDirection().equalsIgnoreCase("ASC")) {
			sort = new Sort(Direction.ASC, para.getSortField());
		} else {
			sort = new Sort(Direction.DESC, para.getSortField());
		}
		PageRequest pageInfo = new PageRequest(pageNumber, pageSize, sort);
		
		if(para.getSkey()!= null && para.getSkey().length()>0)
		{
			Page<ViewTaskAttr> tmpList = this.sysTaskService.getTaskFieldList(para.getSkey(), pageInfo);
			for(ViewTaskAttr tattr: tmpList.getContent())
			{
				
				Map<String,Object> resList = new HashMap<String ,Object>();
				resList.put("taskGuid", tattr.getId().getTaskGuid());
				resList.put("attrGuid", tattr.getId().getAttrGuid());
				resList.put("attrName",tattr.getAttrName());
				resList.put("attrEnum",tattr.getAttrEnum());
				resList.put("attrIsedit",tattr.getAttrIsedit());
				resList.put("attrFgrade",tattr.getAttrFgrade());
				taskUserList.add(resList);
			}
		}
		return ResultUtil.success(taskUserList);
	}
	
	
	@PostMapping(value = "/taskfield/addnew")
	public Result<TlGammaTask> addTaskFieldNew(@RequestBody @Valid TaskParameter taskParam,BindingResult bindingResult) {
		Map<String,Object>  fieldMap = new HashMap<String,Object>();
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		if(taskParam.getTaskGuid()!= null && taskParam.getField()!=null)
		{
			TlGammaKernelAttr kernelattr = new TlGammaKernelAttr();
			kernelattr.setAttrEnum(taskParam.getField().getFieldEnum());
			kernelattr.setAttrFgrade(taskParam.getField().getFieldGrade());
			kernelattr.setAttrName(taskParam.getField().getFieldName());
			kernelattr.setAttrField(this.utilService.generateShortUuid());
			kernelattr.setAttrGuid(UUID.randomUUID().toString());
			kernelattr.setAttrType(taskParam.getField().getFieldType());
			kernelattr.setKernelClassid(taskParam.getClassId());
			
			TlGammaTaskAttr  taskattr = new TlGammaTaskAttr();
			taskattr.setAttrGuid(kernelattr.getAttrGuid());
			taskattr.setTaskGuid(taskParam.getTaskGuid());
			taskattr.setAttrIsedit(taskParam.getField().getIsEdit());
			
			TlGammaTaskAttr tattr = this.sysTaskService.addTaskField(kernelattr,taskattr);
			fieldMap.put("attrGuid", tattr.getAttrGuid());
			fieldMap.put("taskGuid", tattr.getTaskGuid());
		}
		return ResultUtil.success(fieldMap);
	}
	
	@PostMapping(value = "/taskfield/addexist")
	public Result<TlGammaTask> addTaskField(@RequestBody @Valid TaskParameter taskParam,BindingResult bindingResult) {
		Map<String,Object>  fieldMap = new HashMap<String,Object>();
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		if(taskParam.getTaskGuid()!= null && taskParam.getField()!=null)
		{
			TlGammaTaskAttr  taskattr = new TlGammaTaskAttr();
			taskattr.setAttrGuid(taskParam.getField().getFieldGuid());
			taskattr.setTaskGuid(taskParam.getTaskGuid());
			taskattr.setAttrIsedit(taskParam.getField().getIsEdit());
			
			TlGammaTaskAttr tattr = this.sysTaskService.addTaskField(taskattr);
			fieldMap.put("attrGuid", tattr.getAttrGuid());
			fieldMap.put("taskGuid", tattr.getTaskGuid());
		}
		return ResultUtil.success(fieldMap);
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
	
	@PostMapping(value = "/taskref/add")
	public Result<TlGammaTask> taskRef(@RequestBody @Valid TaskParameter taskParam,BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		Map<String,Object> resList = new HashMap<String ,Object>();
		try {
			if(taskParam.getRefClassid() != null && taskParam.getRefExtGuid() != null) {
				this.sysKernelService.transferTaskKernelList(taskParam.getTaskGuid(), taskParam.getRefClassid(),taskParam.getRefExtGuid(),0);
			}
			else if(taskParam.getRefClassid() != null)
			{
				this.sysKernelService.transferTaskKernelList(taskParam.getTaskGuid(), taskParam.getRefClassid(),0);
			}
			//将任务关联的核心对象及标签保存到数据库中
			TlGammaTaskExt taskExt = new TlGammaTaskExt();
			taskExt.setExtGuid(taskParam.getRefExtGuid());
			taskExt.setExtName(taskParam.getRefExtName());
			taskExt.setKernelClassid(taskParam.getRefClassid());
			taskExt.setKernelClassname(taskParam.getRefClassName());
			taskExt.setTaskGuid(taskParam.getTaskGuid());
			taskExt.setKernelGrade(0);
			this.sysTaskService.saveTaskExt(taskExt);
		} catch(Exception e){
			throw new RuntimeException("error");
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
		Optional<TlGammaKernel> kernel = this.sysKernelService.getByKernelClassid(kernelClassid);
		if(kernel.isPresent())
		{
			para.setClassGuid(kernel.get().getKernelClassid());
			para.setClassName(kernel.get().getKernelClassname());
			para.setDescInfo(kernel.get().getKernelClassdesc());
			para.setGeomType(kernel.get().getGeomType());
		}
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
	public Result<Page<ExtTypeParameter>> listKernelExttypes(HttpServletRequest request ,@RequestBody RequestParameter para, BindingResult bindingResult) {
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

		PageRequest pageInfo = PageRequest.of(pageNumber, pageSize, sort);
		
		UserInfo cUser = this.utilService.getCurrentUser(request);
		
		Page<ExtTypeParameter> subList =  this.sysKernelExtService.getKernelExtList( para.getSkey(), cUser.getUserName(),pageInfo);
		
		return ResultUtil.success(subList);
	}
	///创建核心对象子类别
	///更新核心对象子类
	@PostMapping(value = "/kernel/exttype/save")
	public Result<TlGammaKernelExt> addKernelExttype(HttpServletRequest request ,@RequestBody ExtTypeParameter para, BindingResult bindingResult) {
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
			//save spatial filter geometry
			if(para.getFilterGeom()!= null)
			{
				Geometry filterGeom = null;
				try {
					String rangeStr = JSONValue.toJSONString(para.getFilterGeom());
					filterGeom = new GeometryJSON(10).read(rangeStr);
					filterGeom.setSRID(4326);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					filterGeom = null;
					e.printStackTrace();
				}
				if(filterGeom!= null)
				{
					TlGammaKernelGeoFilter geoFilter = new TlGammaKernelGeoFilter();
					geoFilter.setExtGuid(ext.getExtGuid());
					geoFilter.setFilterGuid(UUID.randomUUID().toString());
					geoFilter.setFilterGeom((Polygon) filterGeom);
					this.sysKernelExtService.saveGeoFilter(geoFilter);
				}
			}
			
			for(FilterParameter filter : para.getFilterList())
			{
				if(filter.getAttrGuid()!= null)
				{
					Optional<TlGammaKernelAttr> kernelAttr = this.sysKernelExtService.getKernelAttr(filter.getAttrGuid());
					TlGammaKernelFilter nFilter = new TlGammaKernelFilter();
					nFilter.setFilterGuid(UUID.randomUUID().toString());
					nFilter.setExtGuid(ext.getExtGuid());
					nFilter.setAttrGuid(filter.getAttrGuid());
					nFilter.setFilterType(filter.getType());
					nFilter.setMaxValue(filter.getMaxValue());
					nFilter.setMinValue(filter.getMinValue());
					nFilter.setAttrField(kernelAttr.get().getAttrField());
					nFilter.setAttrType(kernelAttr.get().getAttrType());
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
