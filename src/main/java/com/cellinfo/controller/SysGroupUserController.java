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
import org.springframework.data.domain.PageImpl;
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
import com.cellinfo.controller.entity.AttrParameter;
import com.cellinfo.controller.entity.ExtTypeParameter;
import com.cellinfo.controller.entity.FilterParameter;
import com.cellinfo.controller.entity.GeoFilterParameter;
import com.cellinfo.controller.entity.RequestParameter;
import com.cellinfo.controller.entity.TaskParameter;
import com.cellinfo.controller.entity.TaskUserParameter;
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
	public Result<Page<Map<String,String>>> userTasks(HttpServletRequest request,@RequestBody RequestParameter para, BindingResult bindingResult) {
		UserInfo cUser = this.utilService.getCurrentUser(request);
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		int pageNumber = para.getPage();
		int pageSize = para.getPageSize();
		Sort sort = null; 
		String sortField ="taskName";
		if (para.getSortDirection()!=null && para.getSortDirection().equalsIgnoreCase("ASC")) {
			sort = new Sort(Direction.ASC, sortField);
		} else {
			sort = new Sort(Direction.DESC, sortField);
		}
		PageRequest pageInfo = PageRequest.of(pageNumber, pageSize, sort);

		String filterStr ="";
		if(para.getSkey()!=null&&para.getSkey().length()>0)
		{
			filterStr = para.getSkey();
		}
		try {
			if(para.getIkey() == 0)
			{
				Page<TlGammaTask> tasklist = this.sysTaskService.getUserCreateTasks(cUser.getGroupGuid(),cUser.getUserName(),filterStr,pageInfo);
				if(tasklist!=null)
				{
					List<Map<String,String>> rtasks  = new LinkedList<Map<String,String>>();
					rtasks = tasklist.getContent().stream().map(item -> {
						Map<String,String> tmp = new HashMap<String,String>();
						
						tmp.put("taskId", item.getTaskGuid());
						tmp.put("taskName", item.getTaskName());
						tmp.put("endDate", df.format(item.getTaskTimeend()));
						tmp.put("startDate", df.format(item.getTaskTimestart()));
						tmp.put("userName", item.getUserName());
						if(item.getBusinessPassword()!= null && item.getBusinessPassword().length()>0)
							tmp.put("rPassword", "1");
						else
							tmp.put("rPassword", "0");
						return tmp;
					}).collect(Collectors.toList());
					
					Page<Map<String,String>> resPage = new PageImpl<Map<String,String>>(rtasks,pageInfo,tasklist.getTotalElements());
					return ResultUtil.success(resPage);
				}
			}
			
			Page<ViewTaskUser> viewUserTask =null;
			if(para.getIkey() == 1)
				viewUserTask = this.sysTaskService.getByUserInvolveTasks(cUser.getUserName(),filterStr,pageInfo);
			else if(para.getIkey() == 2)
				viewUserTask = this.sysTaskService.getPasswordTasks(cUser.getGroupGuid(),cUser.getUserName(),filterStr,pageInfo);
			
			if(viewUserTask!=null)
			{
				List<Map<String,String>> rtasks  = new LinkedList<Map<String,String>>();
				rtasks = viewUserTask.getContent().stream().map(item -> {
					Map<String,String> tmp = new HashMap<String,String>();
					
					tmp.put("taskId", item.getId().getTaskGuid());
					tmp.put("taskName", item.getTaskName());
					tmp.put("endDate", df.format(item.getTaskTimeend()));
					tmp.put("startDate", df.format(item.getTaskTimestart()));
					tmp.put("userName", item.getId().getUserName());
					if(item.getBusinessPassword()!= null && item.getBusinessPassword().length()>0)
						tmp.put("rPassword", "1");
					else
						tmp.put("rPassword", "0");
					return tmp;
				}).collect(Collectors.toList());
				
				Page<Map<String,String>> resPage = new PageImpl<Map<String,String>>(rtasks,pageInfo,viewUserTask.getTotalElements());
				return ResultUtil.success(resPage);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResultUtil.error(400,ReturnDesc.UNKNOW_ERROR);
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

	@PostMapping(value = "/task/userlist")
	public Result<List<Map<String ,String>>> taskUserData(HttpServletRequest request) {
		Map<String,Object> resList = new HashMap<String ,Object>();
		
		UserInfo cUser = this.utilService.getCurrentUser(request);
		List<TlGammaUser> userList = this.sysGroupService.getUserByGroup(cUser.getGroupGuid());
		List<Map<String ,String>> userData = new LinkedList<Map<String,String>>();
		for(TlGammaUser user : userList) {
			Map<String ,String > uMap = new HashMap<String,String>();
			uMap.put("userName", user.getUserName());
			uMap.put("userCnname",user.getUserCnname());
			userData.add(uMap);
		}
		return ResultUtil.success(userData);
	}
	
	
	@PostMapping(value = "/task/kernellist")
	public Result<List<Map<String ,String>>> taskKernleData(HttpServletRequest request) {
		Map<String,Object> resList = new HashMap<String ,Object>();
		
		UserInfo cUser = this.utilService.getCurrentUser(request);
		List<TlGammaKernel> kernelClassList = this.sysGroupService.getKernelClassByGroup(cUser.getGroupGuid());
		List<Map<String ,Object>> kernelData = new LinkedList<Map<String,Object>>();
		for(TlGammaKernel kernel : kernelClassList) {
			Map<String ,Object > kMap = new HashMap<String,Object>();
			kMap.put("className",kernel.getKernelClassname());
			kMap.put("classId",kernel.getKernelClassid());
			kMap.put("extList", this.sysKernelExtService.getKernelExtList(kernel.getKernelClassid(), cUser.getUserName()));
			kernelData.add(kMap);
		}
		return ResultUtil.success(kernelData);
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
			//taskExt.setExtName();
			taskExt.setKernelClassid(taskParam.getClassId());
			//taskExt.setKernelClassname();
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
			this.sysKernelService.transferTaskKernelList(taskGuid, kernelClassid,extGuid);
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
		} catch(Exception e){
			throw new RuntimeException("error");
		}
		return ResultUtil.success(resList);
	}
	
	@PostMapping(value = "/taskuser/add")
	public Result<Map<String,Object>> addTaskUser(@RequestBody @Valid TaskUserParameter taskUserParam,BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		Map<String,Object> resList = new HashMap<String ,Object>();
		if(taskUserParam.getTaskGuid()!= null && taskUserParam.getUserName()!=null)
		{
			TlGammaTaskUser taskUser =new TlGammaTaskUser();
			taskUser.setTaskGuid(taskUserParam.getTaskGuid());
			taskUser.setUserName(taskUserParam.getUserName());
			
			TlGammaTaskUser result = this.sysTaskService.saveTaskUser(taskUser);
			resList.put("taskGuid", result.getTaskGuid());
			resList.put("userName", result.getUserName());
		}
		
		return ResultUtil.success(resList);
	}
	@PostMapping(value = "/taskuser/delete")
	public Result<String> deleteTaskUser(@RequestBody @Valid TaskUserParameter taskUserParam,BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		if(taskUserParam.getTaskGuid()!= null && taskUserParam.getUserName()!=null)
		{
			this.sysTaskService.deleteTaskUser(taskUserParam.getTaskGuid(),taskUserParam.getUserName() );
			return ResultUtil.success(ReturnDesc.EXECUTION_SUCCESS);
		}
		
		return ResultUtil.success(ReturnDesc.UNKNOW_ERROR);
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
		PageRequest pageInfo = PageRequest.of(pageNumber, pageSize, sort);
		
		if(para.getSkey()!= null && para.getSkey().length()>0)
		{
			Page<ViewTaskUser> tmpList = this.sysTaskService.getTaskUserList(para.getSkey(), pageInfo);
			for(ViewTaskUser tuser: tmpList.getContent())
			{
				Map<String,Object> resList = new HashMap<String ,Object>();
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
	@PostMapping(value = "/taskattr/list")
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
		PageRequest pageInfo = PageRequest.of(pageNumber, pageSize, sort);
		
		if(para.getSkey()!= null && para.getSkey().length()>0)
		{
			Page<ViewTaskAttr> tmpList = this.sysTaskService.getTaskFieldList(para.getSkey(), pageInfo);
			for(ViewTaskAttr tattr: tmpList.getContent())
			{
				
				Map<String,Object> resList = new HashMap<String ,Object>();
				resList.put("attrId", tattr.getId().getAttrGuid());
				resList.put("attrName",tattr.getAttrName());
				resList.put("attrEnum",tattr.getAttrEnum());
				resList.put("attrIsedit",tattr.getAttrIsedit());
				resList.put("attrFgrade",tattr.getAttrFgrade());
				taskUserList.add(resList);
			}
		}
		return ResultUtil.success(taskUserList);
	}
	
	
	@PostMapping(value = "/taskattr/addnew")
	public Result<Map<String,Object>> addTaskFieldNew(@RequestBody @Valid AttrParameter attrParam,BindingResult bindingResult) {
		Map<String,Object>  fieldMap = new HashMap<String,Object>();
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		if(attrParam.getTaskId()!= null)
		{
			Optional<TlGammaTask> task = this.sysTaskService.getByGuid(attrParam.getTaskId());
			if(task.isPresent())
			{
				TlGammaKernelAttr kernelattr = new TlGammaKernelAttr();
				kernelattr.setAttrEnum(attrParam.getAttrEnum());
				kernelattr.setAttrFgrade(attrParam.getAttrGrade());
				kernelattr.setAttrName(attrParam.getAttrName());
				kernelattr.setAttrField(this.utilService.generateShortUuid());
				kernelattr.setAttrGuid(UUID.randomUUID().toString());
				kernelattr.setAttrType(attrParam.getAttrType());
				kernelattr.setKernelClassid(task.get().getKernelClassid());
				
				TlGammaTaskAttr  taskattr = new TlGammaTaskAttr();
				taskattr.setAttrGuid(kernelattr.getAttrGuid());
				taskattr.setTaskGuid(attrParam.getTaskId());
				taskattr.setAttrIsedit(attrParam.getIsEdit());
				taskattr.setLayerGrade(1);//可编辑图层
				
				TlGammaTaskAttr tattr = this.sysTaskService.addTaskField(kernelattr,taskattr);
				fieldMap.put("attrId", tattr.getAttrGuid());
				fieldMap.put("taskId", tattr.getTaskGuid());
				return ResultUtil.success(fieldMap);
			}
		}
		return ResultUtil.error(400,  ReturnDesc.UNKNOW_ERROR);
	}
	
	@PostMapping(value = "/taskattr/addexist")
	public Result<Map<String,Object>> addTaskFieldExist(@RequestBody @Valid AttrParameter attrParam,BindingResult bindingResult) {
		Map<String,Object>  fieldMap = new HashMap<String,Object>();
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		if(attrParam.getTaskId()!= null && attrParam.getAttrId()!=null)
		{
			TlGammaTaskAttr  taskattr = new TlGammaTaskAttr();
			taskattr.setAttrGuid(attrParam.getAttrId());
			taskattr.setTaskGuid(attrParam.getTaskId());
			taskattr.setAttrIsedit(attrParam.getIsEdit());
			taskattr.setLayerGrade(1);
			
			TlGammaTaskAttr tattr = this.sysTaskService.addTaskField(taskattr);
			fieldMap.put("attrGuid", tattr.getAttrGuid());
			fieldMap.put("taskGuid", tattr.getTaskGuid());
		}
		return ResultUtil.success(fieldMap);
	}
	
	@PostMapping(value = "/taskattr/delete")
	public Result<String> deleteTaskField(@RequestBody @Valid AttrParameter attrParam,BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		if(attrParam.getTaskId()!= null && attrParam.getAttrId()!=null)
		{
			this.sysTaskService.deleteTaskAttr(attrParam.getTaskId(),attrParam.getAttrId());
			return ResultUtil.success(ReturnDesc.EXECUTION_SUCCESS);
		}
		
		return ResultUtil.error(400,  ReturnDesc.UNKNOW_ERROR);
	}
	
	@PostMapping(value = "/taskref/add")
	public Result<Map<String,Object>> taskRef(@RequestBody @Valid TaskParameter taskParam,BindingResult bindingResult) {
		Map<String,Object>  fMap = new HashMap<String,Object>();
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		Map<String,Object> resList = new HashMap<String ,Object>();
		try {
			if(taskParam.getRefClassid() != null)
			{
				//将任务关联的核心对象及标签保存到数据库中
				TlGammaTaskExt taskExt = new TlGammaTaskExt();
				taskExt.setKernelClassid(taskParam.getRefClassid());
				//taskExt.setKernelClassname();
				taskExt.setTaskGuid(taskParam.getTaskGuid());
				taskExt.setKernelGrade(0);
				TlGammaTaskExt tmpExt = this.sysTaskService.saveTaskExt(taskExt);
				
				fMap.put("refclassId", tmpExt.getKernelClassid());
				fMap.put("taskId", tmpExt.getTaskGuid());
			}
		} catch(Exception e){
			throw new RuntimeException("error");
		}
		return ResultUtil.success(fMap);
	}
	
	@PostMapping(value = "/taskref/addattr")
	public Result<Map<String,Object>> addTaskRefField(@RequestBody @Valid AttrParameter attrParam,BindingResult bindingResult) {
		Map<String,Object>  fieldMap = new HashMap<String,Object>();
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		if(attrParam.getTaskId()!= null && attrParam.getAttrId()!=null)
		{
			TlGammaTaskAttr  taskattr = new TlGammaTaskAttr();
			taskattr.setAttrGuid(attrParam.getAttrId());
			taskattr.setTaskGuid(attrParam.getTaskId());
			taskattr.setAttrIsedit(0);
			taskattr.setLayerGrade(0);
			
			TlGammaTaskAttr tattr = this.sysTaskService.addTaskField(taskattr);
			fieldMap.put("refattrId", tattr.getAttrGuid());
			fieldMap.put("taskId", tattr.getTaskGuid());
		}
		return ResultUtil.success(fieldMap);
	}
	
	@PostMapping(value = "/closedtasks")
	public Result<String> groupList(@RequestBody String taskId, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		logger.info("closedtasks");
		
		return ResultUtil.success("");
	}
	
	@PostMapping(value = "/kernels")
	public Result<Page<Map<String, Object>>> kernelList(HttpServletRequest request ,@RequestBody RequestParameter para, BindingResult bindingResult) {
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
		
		if (para.getSortDirection().equalsIgnoreCase("ASC")) {
			sort = new Sort(Direction.ASC, sortField);
		} else {
			sort = new Sort(Direction.DESC, sortField);
		}

		PageRequest pageInfo = PageRequest.of(pageNumber, pageSize, sort);
		Page<TlGammaKernel> mList = this.sysKernelService.getGroupKernelList(cUser.getGroupGuid(),pageInfo);
		for (TlGammaKernel eachKernel : mList) { 
			Map<String, Object> tMap = new HashMap<String, Object>();
			tMap.put("classGuid", eachKernel.getKernelClassid());
			tMap.put("className", eachKernel.getKernelClassname());
			tMap.put("descInfo", eachKernel.getKernelClassdesc());
			list.add(tMap);
		}
		
		Page<Map<String, Object>> resPage = new PageImpl<Map<String, Object>>(list,pageInfo,mList.getTotalElements());
		return ResultUtil.success(resPage);
	}
	
	///核对象信息
	@PostMapping(value = "/kernel/info")
	public Result<Map<String,Object>> getKernelInfo(@RequestBody String kernelClassid, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		logger.info("userList");
		Map<String,Object> result = new HashMap<String,Object>();
		Optional<TlGammaKernel> kernel = this.sysKernelService.getByKernelClassid(kernelClassid);
		if(kernel.isPresent())
		{
			result.put("classId",kernel.get().getKernelClassid());
			result.put("className",kernel.get().getKernelClassname());
			result.put("descInfo",kernel.get().getKernelClassdesc());
			result.put("geoType",kernel.get().getGeomType());
		}
		List<TlGammaKernelAttr>  attrList = this.sysKernelService.getKernelAttrList(kernelClassid);
		
		List<Map<String,Object>> fieldList = attrList.stream().map(item ->{
			Map<String,Object> fieldMap = new HashMap<String,Object>();
			fieldMap.put("attrId", item.getAttrGuid());
			fieldMap.put("attrName",item.getAttrName());
			fieldMap.put("attrType",item.getAttrType());
			fieldMap.put("attrGrade",item.getAttrFgrade());
			fieldMap.put("attrEnum",item.getAttrEnum());
			return fieldMap;
		}).collect(Collectors.toList());
		
		result.put("attrs", fieldList);
		return ResultUtil.success(result);
	}
		
	
	@PostMapping(value = "/kernel/exttypes")
	public Result<Page<Map<String ,String >>> listKernelExttypes(HttpServletRequest request ,@RequestBody RequestParameter para, BindingResult bindingResult) {
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
		
		Page<Map<String ,String >> subList =  this.sysKernelExtService.getKernelExtList( para.getSkey(), cUser.getUserName(),pageInfo);
		
		return ResultUtil.success(subList);
	}
	///创建核心对象子类别
	@PostMapping(value = "/kernel/exttype/save")
	public Result<Map<String,String>> addKernelExttype(HttpServletRequest request ,@RequestBody ExtTypeParameter para, BindingResult bindingResult) {
		Map<String,String> rMap = new HashMap<String,String>();
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		UserInfo cUser = this.utilService.getCurrentUser(request);

		List<TlGammaKernelExt> extList = this.sysKernelExtService.getKernelExtByName(para.getExtName(),cUser.getUserName());
		if(extList!= null && extList.size()>0)
			return ResultUtil.error(400, ReturnDesc.EXT_NAME_IS_EXIST);
		
		TlGammaKernelExt ext = new TlGammaKernelExt(); 
		ext.setExtGuid(UUID.randomUUID().toString());
		ext.setExtDesc(para.getExtDesc());
		ext.setExtName(ext.getExtName());
		ext.setKernelClassid(para.getKernelClassid());
		ext.setUserName(cUser.getUserName());

		TlGammaKernelExt tmp = this.sysKernelExtService.saveKernelExt(ext);
		rMap.put("extName", tmp.getExtName());
		rMap.put("extGuid", tmp.getExtGuid());
		return ResultUtil.success(rMap);
	}
	
	///更新核心对象子类
	@PostMapping(value = "/kernel/exttype/update")
	public Result<TlGammaKernelExt> updateKernelExttype(HttpServletRequest request ,@RequestBody ExtTypeParameter para, BindingResult bindingResult) {
		Map<String,String> rMap = new HashMap<String,String>();
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}

		if(para.getExtGuid()!= null)
		{
			Optional<TlGammaKernelExt> kernelExt =  this.sysKernelExtService.getKernelExt(para.getExtGuid());
			if(kernelExt.isPresent())
			{
				TlGammaKernelExt tmpExt = kernelExt.get();
				if(para.getExtName()!= null)
					tmpExt.setExtName(para.getExtName());
				if(para.getExtDesc()!= null)
					tmpExt.setExtDesc(para.getExtDesc());
				TlGammaKernelExt saveExt = this.sysKernelExtService.saveKernelExt(tmpExt);
				rMap.put("extName", saveExt.getExtName());
				rMap.put("extGuid", saveExt.getExtGuid());
				return ResultUtil.success(rMap);
			}
		}
		return ResultUtil.error(400, ReturnDesc.UNKNOW_ERROR);
	}
	
	@PostMapping(value = "/kernel/exttype/addfilter")
	public Result<Map<String,String>> addFilterToKernelExttype(HttpServletRequest request ,@RequestBody FilterParameter para, BindingResult bindingResult) {
		Map<String,String> rMap = new HashMap<String,String>();
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		
		if(para!= null)
		{
			TlGammaKernelFilter nFilter = new TlGammaKernelFilter();
			nFilter.setFilterGuid(UUID.randomUUID().toString());
			nFilter.setExtGuid(para.getExtGuid());
			nFilter.setAttrGuid(para.getAttrGuid());
			nFilter.setMaxValue(para.getMaxValue());
			nFilter.setMinValue(para.getMinValue());
			
			TlGammaKernelFilter saveFilter = this.sysKernelExtService.saveFilter(nFilter);
			rMap.put("filterId", saveFilter.getFilterGuid());
			rMap.put("extGuid", saveFilter.getExtGuid());
			return ResultUtil.success(rMap);
			
		}
		return ResultUtil.error(400, ReturnDesc.UNKNOW_ERROR);
	}
	
	@PostMapping(value = "/kernel/exttype/deletefilter")
	public Result<String> deleteFilterToKernelExttype(HttpServletRequest request ,@RequestBody FilterParameter para, BindingResult bindingResult) {
		Map<String,String> rMap = new HashMap<String,String>();
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		
		if(para.getFilterId()!= null)
		{
			this.sysKernelExtService.deleteFilter(para.getFilterId());
			return ResultUtil.success(ReturnDesc.EXECUTION_SUCCESS);
			
		}
		return ResultUtil.error(400, ReturnDesc.UNKNOW_ERROR);
	}
	
	@PostMapping(value = "/kernel/exttype/addgeofilter")
	public Result<TlGammaKernelExt> addGeoFilterToKernelExttype(HttpServletRequest request ,@RequestBody GeoFilterParameter para, BindingResult bindingResult) {
		Map<String,String> rMap = new HashMap<String,String>();
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		
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
				geoFilter.setExtGuid(para.getExtGuid());
				geoFilter.setFilterGuid(UUID.randomUUID().toString());
				geoFilter.setFilterGeom((Polygon) filterGeom);
				TlGammaKernelGeoFilter saveFilter = this.sysKernelExtService.saveGeoFilter(geoFilter);
				rMap.put("filterId", saveFilter.getFilterGuid());
				rMap.put("extGuid", saveFilter.getExtGuid());
				return ResultUtil.success(rMap);
			}
		}
		return ResultUtil.error(400, ReturnDesc.UNKNOW_ERROR);
	}
	
	@PostMapping(value = "/kernel/exttype/deletegeofilter")
	public Result<String> deleteGeoFilterToKernelExttype(HttpServletRequest request ,@RequestBody GeoFilterParameter para, BindingResult bindingResult) {
		Map<String,String> rMap = new HashMap<String,String>();
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		
		if(para.getFilterId()!= null)
		{
			this.sysKernelExtService.deleteGeoFilter(para.getFilterId());
			return ResultUtil.success(ReturnDesc.EXECUTION_SUCCESS);
			
		}
		return ResultUtil.error(400, ReturnDesc.UNKNOW_ERROR);
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
