package com.cellinfo.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cellinfo.annotation.OperLog;
import com.cellinfo.annotation.ServiceLog;
import com.cellinfo.controller.entity.AttrParameter;
import com.cellinfo.controller.entity.ExtTypeParameter;
import com.cellinfo.controller.entity.FilterParameter;
import com.cellinfo.controller.entity.GAExtParameter;
import com.cellinfo.controller.entity.GAFilter;
import com.cellinfo.controller.entity.GAGeoFilter;
import com.cellinfo.controller.entity.GAParameter;
import com.cellinfo.controller.entity.GeoFilterParameter;
import com.cellinfo.controller.entity.RequestParameter;
import com.cellinfo.controller.entity.TaskAttrParameter;
import com.cellinfo.controller.entity.TaskLayerParameter;
import com.cellinfo.controller.entity.TaskParameter;
import com.cellinfo.controller.entity.TaskUserParameter;
import com.cellinfo.entity.Result;
import com.cellinfo.entity.TlGammaDict;
import com.cellinfo.entity.TlGammaKernel;
import com.cellinfo.entity.TlGammaKernelAttr;
import com.cellinfo.entity.TlGammaKernelExt;
import com.cellinfo.entity.TlGammaKernelFilter;
import com.cellinfo.entity.TlGammaKernelGeoFilter;
import com.cellinfo.entity.TlGammaTask;
import com.cellinfo.entity.TlGammaTaskAttr;
import com.cellinfo.entity.TlGammaTaskAttrrank;
import com.cellinfo.entity.TlGammaTaskLayer;
import com.cellinfo.entity.TlGammaTaskUser;
import com.cellinfo.entity.TlGammaUser;
import com.cellinfo.entity.ViewTaskAttr;
import com.cellinfo.entity.ViewTaskExt;
import com.cellinfo.entity.ViewTaskUser;
import com.cellinfo.security.UserInfo;
import com.cellinfo.service.SysBusdataService;
import com.cellinfo.service.SysDictService;
import com.cellinfo.service.SysGroupService;
import com.cellinfo.service.SysKernelExtService;
import com.cellinfo.service.SysKernelService;
import com.cellinfo.service.SysTaskService;
import com.cellinfo.service.SysUserService;
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
	private SysUserService sysUserService;
	
	@Autowired
	private SysTaskService sysTaskService ;
	
	@Autowired
	private SysKernelService sysKernelService;
	
	@Autowired
	private SysGroupService sysGroupService;
	
	@Autowired
	private SysKernelExtService sysKernelExtService;
	
	@Autowired
	private SysDictService sysDictService;
	
	@Autowired
	private SysBusdataService sysBusdataService;
	
	private RestTemplate restTemplate = new RestTemplate();
	
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
	
	private DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");  
	
	
	@PostMapping(value = "/kernel/addattr")
	public Result<Map<String,String>> addKernelAttr(HttpServletRequest request ,@RequestBody @Valid AttrParameter attrPara, BindingResult bindingResult) {
		UserInfo cUser = this.utilService.getCurrentUser(request);
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		if(attrPara.getClassId()!=null && attrPara.getClassId().length()>0)
		{
			Optional<TlGammaKernel> optionalkernel = this.sysKernelService.getByKernelClassid(attrPara.getClassId());
			if(optionalkernel.isPresent())
			{
				List<TlGammaKernelAttr> tmpattrs = this.sysKernelService.getAttrByName(attrPara.getClassId(),attrPara.getAttrName());
				if(tmpattrs!= null && tmpattrs.size()>0)
				{
					return ResultUtil.error(400, ReturnDesc.ATTR_NAME_IS_EXIST);
				}
				
				Map<String,String> result = new HashMap<String,String>();
				TlGammaKernelAttr  attr = new TlGammaKernelAttr();
				attr.setAttrGuid(UUID.randomUUID().toString());
				attr.setKernelClassid(attrPara.getClassId());
				attr.setAttrName(attrPara.getAttrName());
				attr.setAttrField("F_"+this.utilService.generateShortUuid());
				attr.setAttrType(attrPara.getAttrType());
				attr.setDictId(attrPara.getAttrId());
				attr.setAttrFgrade(attrPara.getAttrGrade());
				attr.setAttrMax(attrPara.getMaxValue());
				attr.setAttrMin(attrPara.getMinValue());
				attr.setAttrDesc(attrPara.getAttrDesc());
				attr.setDictId(attrPara.getDictId());
				attr.setUserName(cUser.getUserName());
				if(attrPara.getShareGrade() != null && 
						(attrPara.getShareGrade().equalsIgnoreCase("GROUP") ||
						 attrPara.getShareGrade().equalsIgnoreCase("TASK")))
				{
					attr.setShareGrade(attrPara.getShareGrade().toUpperCase());
				}
				else
				{
					attr.setShareGrade("GROUP");
				}
				attr.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				attr.setCreateTime(new Timestamp(System.currentTimeMillis()));
				TlGammaKernelAttr  tmp = this.sysKernelService.saveKernelAttr(attr);
		
				result.put("attrId", tmp.getAttrGuid());
				result.put("attrName", tmp.getAttrName());
				return ResultUtil.success(result);
			}
		}
		return ResultUtil.error(400,ReturnDesc.UNKNOW_ERROR);
	}
	
	@PostMapping(value = "/kernel/updateattr")
	public Result<String> updateKernelAttr(HttpServletRequest request ,@RequestBody @Valid AttrParameter attrPara, BindingResult bindingResult) {
		UserInfo cUser = this.utilService.getCurrentUser(request);
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		Map<String,String> result = new HashMap<String,String>();
		Optional<TlGammaKernelAttr> optionAttr = this.sysKernelService.getAttrById(attrPara.getAttrId());
		if(!optionAttr.isPresent())
		{
			return ResultUtil.error(400,ReturnDesc.THIS_ATTR_IS_NOT_EXIST);
		}
		TlGammaKernelAttr attr = optionAttr.get();
		if(attrPara.getAttrType()!= null)
			attr.setAttrType(attrPara.getAttrType());
		if(attrPara.getAttrDesc()!= null)
			attr.setAttrDesc(attrPara.getAttrDesc());
		if(attrPara.getAttrGrade()!=null)
			attr.setAttrFgrade(attrPara.getAttrGrade());
		if(attrPara.getMaxValue()!=null)
			attr.setAttrMax(attrPara.getMaxValue());
		if(attrPara.getMinValue()!=null)
			attr.setAttrMin(attrPara.getMinValue());
		if(attrPara.getDictId()!=null)
			attr.setDictId(attrPara.getDictId());
		if(attrPara.getShareGrade() != null && 
				(attrPara.getShareGrade().equalsIgnoreCase("GROUP") ||
				 attrPara.getShareGrade().equalsIgnoreCase("TASK")))
		{
			attr.setShareGrade(attrPara.getShareGrade().toUpperCase());
		}
		else
		{
			attr.setShareGrade("GROUP");
		}
		
		attr.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		this.sysKernelService.updateKernelAttr(attr);
		
		return ResultUtil.success("ok");
	}
	
	@PostMapping(value = "/kernel/deleteattr")
	public Result<String> deleteKernelAttr(HttpServletRequest request ,@RequestBody @Valid AttrParameter attrPara, BindingResult bindingResult) {
		UserInfo cUser = this.utilService.getCurrentUser(request);
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		Map<String,String> result = new HashMap<String,String>();
		
		//属性字段被标签使用数量
		long extNum = this.sysTaskService.getExtApplyNum(attrPara.getAttrId());
		//属性字段被任务使用数量
		long aNum = this.sysTaskService.getAttrApplyNum(attrPara.getAttrId());
		if(aNum < 1 && extNum <1)///判断该字段是否被使用
		{
			this.sysKernelService.deleteKernelAttr(attrPara.getAttrId());
			return ResultUtil.success(ReturnDesc.EXECUTION_SUCCESS);
		}
		return ResultUtil.error(400,ReturnDesc.THIS_ATTR_IS_INUSED);
	}
	
	@PostMapping(value = "/kernel/attrapplynum")
	public Result<Map<String,Long>> kernelAttrApplyNumInTask(HttpServletRequest request ,@RequestBody @Valid AttrParameter attrPara, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		Map<String,Long> result = new HashMap<String,Long>();
		Long aNum = this.sysTaskService.getAttrApplyNum(attrPara.getAttrId());
		
		result.put("applynum",aNum);
		return ResultUtil.success(result);
	}
	
	@PostMapping(value = "/task/layerinfo")
	public Result<List<Map<String, String>>> queryTaskLayerInfo( @RequestBody String taskId) {
		List<Map<String, String>> layerlist =  new LinkedList<Map<String, String>>();

		try {
				List<ViewTaskExt> taskExtList =  this.sysBusdataService.getTaskLayerList(taskId);
				layerlist = taskExtList.stream().map(item ->{
					Map<String,String> layer = new HashMap<String,String>();
					layer.put("layerId",item.getLayerGuid());
					layer.put("className", item.getKernelClassname());
					layer.put("extName", item.getExtName());
					layer.put("geoType", item.getGeomType());
					layer.put("layerGrade", String.valueOf(item.getLayerGrade()));
					return layer;
				}).collect(Collectors.toList());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultUtil.success(layerlist);
	}
	
	// ikey 0---用户创建任务，1---用户参与任务，2-口令任务
	
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
		String sortField = "updateTime";
		if (para.getSortDirection()!=null && para.getSortDirection().equalsIgnoreCase("ASC")) {
			sort = new Sort(Direction.ASC, sortField);
		} else {
			sort = new Sort(Direction.DESC, sortField);
		}
		PageRequest pageInfo =new  PageRequest(pageNumber, pageSize, sort);

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
						tmp.put("terminalTime", df.format(item.getTerminalTime()));
						tmp.put("startTime", df.format(item.getStartTime()));
						tmp.put("userName", item.getUserName());
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
					tmp.put("terminalTime", df.format(item.getTerminalTime()));
					tmp.put("startTime", df.format(item.getStartTime()));
					tmp.put("userName", item.getId().getUserName());
					return tmp;
				}).collect(Collectors.toList());
				
				Page<Map<String,String>> resPage = new PageImpl<Map<String,String>>(rtasks,pageInfo,viewUserTask.getTotalElements());
				return ResultUtil.success(resPage);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultUtil.error(400,ReturnDesc.UNKNOW_ERROR);
	}
	
	@PostMapping(value = "/task")
	public Result<TaskParameter> queryTaskInfo(@RequestBody GAParameter para) {
		Map<String,String> tMap = new HashMap<String,String>();

		Optional<TlGammaTask> optionaltask = this.sysTaskService.getByGuid(para.getId());
		if(optionaltask.isPresent())
		{
			TlGammaTask item = optionaltask.get();
			tMap.put("taskId", item.getTaskGuid());
			tMap.put("taskName", item.getTaskName());
			tMap.put("startTime", df.format(item.getStartTime()));
			tMap.put("terminalTime", df.format(item.getTerminalTime()));
			tMap.put("userName", item.getUserName());
			tMap.put("busPassword",item.getBusinessPassword());
		}
		return ResultUtil.success(tMap);
	}

	@PostMapping(value = "/task/user/avaliable")
	public Result<List<Map<String ,String>>> taskKernelAvaliable(HttpServletRequest request,@RequestBody TaskParameter taskParam) {
		List<Map<String,Object>> resList = new LinkedList<Map<String ,Object>>();
		
		UserInfo cUser = this.utilService.getCurrentUser(request);
		List<TlGammaUser> userList = this.sysUserService.getGroupMemberAvaliable(cUser.getGroupGuid(),taskParam.getTaskId());
		if(userList!=null && userList.size()>0)
		{
			for(TlGammaUser user : userList) {
				Map<String ,Object > kMap = new HashMap<String,Object>();
				kMap.put("userName",user.getUserName());
				kMap.put("userCnname",user.getUserCnname());
				resList.add(kMap);
			}
		}
		return ResultUtil.success(resList);
	}
	
	@PostMapping(value = "/task/attr/avaliable")
	public Result<List<Map<String ,String>>> taskAttrAvaliable(HttpServletRequest request,@RequestBody TaskParameter taskParam) {
		List<Map<String,Object>> resList = new LinkedList<Map<String ,Object>>();
		
		UserInfo cUser = this.utilService.getCurrentUser(request);
		if(taskParam.getTaskId()!=null)
		{
			Optional<TlGammaTask> optionaltask = this.sysTaskService.getTaskbyId(taskParam.getTaskId());
			if(optionaltask.isPresent())
			{
				List<TlGammaKernelAttr>  attrList = null;
				if( taskParam.getRefClassId() == null)
				{
					attrList = this.sysKernelService.getTaskAttrAvalialble(taskParam.getRefClassId(),cUser.getUserName(),taskParam.getTaskId());
				}
				else
				{
					attrList = this.sysKernelService.getTaskAttrAvalialble(optionaltask.get().getKernelClassid(),cUser.getUserName(),taskParam.getTaskId());
				}
				if(attrList!=null && attrList.size()>0)
				{
					for(TlGammaKernelAttr  attr : attrList)
					{
						Map<String ,Object > kMap = new HashMap<String,Object>();
						kMap.put("attrId",attr.getAttrGuid());
						kMap.put("attrName",attr.getAttrName());
						resList.add(kMap);
					}
				}
			}
		}
		return ResultUtil.success(resList);
	}
		
	
	@PostMapping(value = "/task/kernel/avaliable")
	public Result<List<Map<String ,String>>> taskKernleData(HttpServletRequest request,@RequestBody TaskParameter taskParam) {
		List<Map<String,Object>> resList = new LinkedList<Map<String ,Object>>();
		
		UserInfo cUser = this.utilService.getCurrentUser(request);
		
		List<TlGammaKernel>  kernerlList =  this.sysKernelService.getTaskKernelAvaliable(cUser.getGroupGuid(),taskParam.getTaskId());
		for(TlGammaKernel kernel : kernerlList) {
			Map<String ,Object > kMap = new HashMap<String,Object>();
			kMap.put("className",kernel.getKernelClassname());
			kMap.put("classId",kernel.getKernelClassid());
			kMap.put("extList", this.sysKernelExtService.getKernelExtList(kernel.getKernelClassid(), cUser.getUserName()));
			resList.add(kMap);
		}
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
			if(taskParam.getTaskName()!= null)
			{
				TlGammaTask task = new TlGammaTask();
				String taskGuid = UUID.randomUUID().toString();
				task.setTaskGuid(taskGuid);
				task.setTaskName(taskParam.getTaskName());
				task.setStartTime(new Timestamp(dateformat.parse(taskParam.getStartTime()).getTime()));
				task.setTerminalTime(new Timestamp(dateformat.parse(taskParam.getTerminalTime()).getTime()));
				task.setUserName(cUser.getUserName());
				task.setGroupGuid(cUser.getGroupGuid());
				task.setTaskDesc(taskParam.getTaskDesc());
				//classId和extId必选其一，否则提示错误
				//将任务关联的核心对象及标签保存到数据库中
				TlGammaTaskLayer taskLayer = new TlGammaTaskLayer();
				taskLayer.setLayerGuid(UUID.randomUUID().toString());
				if(taskParam.getExtId() != null && taskParam.getExtId().length()>0)
				{
					Optional<TlGammaKernelExt> optionalExt = this.sysKernelExtService.getExtById(taskParam.getExtId());
					if(optionalExt.isPresent())
					{
						taskLayer.setExtGuid(taskParam.getExtId());
						taskLayer.setKernelClassid(optionalExt.get().getKernelClassid());
						taskLayer.setTaskGuid(taskGuid);
						taskLayer.setLayerName(optionalExt.get().getExtName());
						//任务主图层
						taskLayer.setLayerGrade(1);
						this.sysTaskService.saveTaskLayer(taskLayer);
						
						task.setKernelClassid(optionalExt.get().getKernelClassid());
					}
				}
				else if (taskParam.getClassId() != null && taskParam.getClassId().length()>0)
				{
					Optional<TlGammaKernel> optionakernel =this.sysKernelService.getByKernelClassid(taskParam.getClassId());
					if(optionakernel.isPresent())
					{
						taskLayer.setKernelClassid(optionakernel.get().getKernelClassid());
						taskLayer.setTaskGuid(taskGuid);
						taskLayer.setLayerName(optionakernel.get().getKernelClassname());
						//任务主图层
						taskLayer.setLayerGrade(1);
						this.sysTaskService.saveTaskLayer(taskLayer);
						
						task.setKernelClassid(taskParam.getClassId());
					}
				}
				else
				{
					return ResultUtil.error(400, ReturnDesc.KERNEL_CLASS_SHOULD_NOT_NULL);
				}
				/**
				 * 该任务能否新建核心对象
				 */
				task.setKernelAdd(taskParam.getKernelAdd());
				if(taskParam.getBusPassword()!=null)
					task.setBusinessPassword(taskParam.getBusPassword());
				
				task.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				task.setCreateTime(new Timestamp(System.currentTimeMillis()));
				TlGammaTask tmpTask = this.sysTaskService.addTask(task);
				
				///添加任务属性分组
				for(int i =0;i<5;i++)
				{
					TlGammaTaskAttrrank rank1 = new TlGammaTaskAttrrank();
					rank1.setRankGuid(UUID.randomUUID().toString());
					rank1.setTaskGuid(taskGuid);
					rank1.setRankName("分组"+(i+1));
					
					this.sysTaskService.saveTaskAttrRank(rank1);
				}
				
				resList.put("taskGuid", tmpTask.getTaskGuid());
				resList.put("taskName", tmpTask.getTaskName());
			}
		} catch(Exception e){
			throw new RuntimeException(e);
		}

		return ResultUtil.success(resList);
	}

	@PostMapping(value = "/task/update")
	public Result<Map<String,Object>> updateTask(@RequestBody @Valid TaskParameter taskParam,BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		Map<String,Object> resList = new HashMap<String ,Object>();
		try {
			if(taskParam.getTaskId()!= null)
			{
				Optional<TlGammaTask> taskOptional = this.sysTaskService.getByGuid(taskParam.getTaskId());
				if(taskOptional.isPresent())
				{
					TlGammaTask task = taskOptional.get();
					
					if(taskParam.getTaskName()!=null)
						task.setTaskName(taskParam.getTaskName());
					if(taskParam.getTaskDesc()!=null)
						task.setTaskDesc(taskParam.getTaskDesc());
					if(taskParam.getStartTime()!=null)
						task.setStartTime(new Timestamp(dateformat.parse(taskParam.getStartTime()).getTime()));
					if(taskParam.getTerminalTime()!=null)
						task.setTerminalTime(new Timestamp(dateformat.parse(taskParam.getTerminalTime()).getTime()));
					if(taskParam.getBusPassword()!=null)
						task.setBusinessPassword(taskParam.getBusPassword());
					task.setUpdateTime(new Timestamp(System.currentTimeMillis()));
					TlGammaTask tmpTask = this.sysTaskService.updateTask(task);
					resList.put("taskGuid", tmpTask.getTaskGuid());
					resList.put("taskName", tmpTask.getTaskName());
					return ResultUtil.success(resList);
				}
			}
		} catch(Exception e){
			throw new RuntimeException(e);
		}
		return ResultUtil.success(ReturnDesc.UNKNOW_ERROR);
	}
	
	@PostMapping(value = "/taskuser/add")
	public Result<Map<String,Object>> addTaskUser(@RequestBody @Valid TaskUserParameter taskUserParam,BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		Map<String,Object> resList = new HashMap<String ,Object>();
		if(taskUserParam.getTaskId()!= null && taskUserParam.getUserName()!=null)
		{
			Optional<ViewTaskUser> optionaltaskuser =  this.sysTaskService.getTaskUser(taskUserParam.getTaskId(), taskUserParam.getUserName());
			if(!optionaltaskuser.isPresent())
			{
				TlGammaTaskUser taskUser =new TlGammaTaskUser();
				taskUser.setTaskGuid(taskUserParam.getTaskId());
				taskUser.setUserName(taskUserParam.getUserName());
				
				TlGammaTaskUser result = this.sysTaskService.saveTaskUser(taskUser);
				resList.put("taskId", result.getTaskGuid());
				resList.put("userName", result.getUserName());
				return ResultUtil.success(resList);
			}
			else
			{
				return ResultUtil.success(ReturnDesc.TASK_USER_IS_EXIST);
			}
		}
		return ResultUtil.success(ReturnDesc.UNKNOW_ERROR);
	}
	@PostMapping(value = "/taskuser/delete")
	public Result<String> deleteTaskUser(@RequestBody @Valid TaskUserParameter taskUserParam,BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		if(taskUserParam.getTaskId()!= null && taskUserParam.getUserName()!=null)
		{
			this.sysTaskService.deleteTaskUser(taskUserParam.getTaskId(),taskUserParam.getUserName() );
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
		String sortField = "updateTime";
		if (para.getSortDirection()!=null && para.getSortDirection().equalsIgnoreCase("ASC")) {
			sort = new Sort(Direction.ASC, sortField);
		} else {
			sort = new Sort(Direction.DESC, sortField);
		}
		
		PageRequest pageInfo = new PageRequest(pageNumber, pageSize, sort);
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
			Page<Map<String,Object>> resPage = new PageImpl<Map<String,Object>>(taskUserList,pageInfo,tmpList.getTotalElements());
			return ResultUtil.success(resPage);
		}
		return ResultUtil.error(400, ReturnDesc.UNKNOW_ERROR);
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
		PageRequest pageInfo = new PageRequest(pageNumber, pageSize, sort);
		
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
				resList.put("rankId", tattr.getRankGuid());
				taskUserList.add(resList);
			}
			Page<Map<String,Object>> resPage = new PageImpl<Map<String,Object>>(taskUserList,pageInfo,tmpList.getTotalElements());
			return ResultUtil.success(resPage);
		}
		return ResultUtil.error(400, ReturnDesc.UNKNOW_ERROR);
	}
	// TODO
	//添加更新作务字段 分组及编辑状态的接口
	
	@PostMapping(value = "/taskattr/update")
	public Result<Map<String,Object>> taskAttrUpdate(@RequestBody @Valid TaskAttrParameter para,BindingResult bindingResult) {
		
		Map<String, Object> attrMap =  new HashMap<String, Object>();
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		
		if(para.getTaskId()!= null && para.getAttrId()!= null)
		{
			TlGammaTaskAttr  taskAttr = this.sysTaskService.getTaskAttr(para.getTaskId(),para.getAttrId());
			if(taskAttr!= null)
			{
				if(para.getIsEdit()!= null)
					taskAttr.setAttrIsedit(para.getIsEdit());
				if(para.getRankId()!=null)
					taskAttr.setRankGuid(para.getRankId());
				TlGammaTaskAttr tmp = this.sysTaskService.saveTaskAttr(taskAttr);
				attrMap.put("taskId", tmp.getTaskGuid());
				attrMap.put("attrId", tmp.getAttrGuid());
				return ResultUtil.success(attrMap);
			}
			else
			{
				return ResultUtil.error(400, ReturnDesc.THIS_ATTR_IS_NOT_EXIST);
			}
		}
		return ResultUtil.error(400, ReturnDesc.UNKNOW_ERROR);
	}
	//添加更新字段分组值的接口
	
	@PostMapping(value = "/taskattr/ranklist")
	public Result<List<Map<String,Object>>> taskAttrRankList(@RequestBody @Valid TaskAttrParameter para,BindingResult bindingResult) {
		
		List<Map<String, Object>> rankList =  new LinkedList<Map<String, Object>>();
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		
		if(para.getTaskId()!= null && para.getTaskId().length()>0)
		{
			List<TlGammaTaskAttrrank> attrrankList =  this.sysTaskService.getTaskAttrRankList(para.getTaskId());
			for(TlGammaTaskAttrrank rank : attrrankList)
			{
				Map<String,Object> rankMap = new HashMap<String,Object>();
				rankMap.put("rankId", rank.getRankGuid());
				rankMap.put("rankName", rank.getRankName());
				rankList.add(rankMap);
			}
			
		}
		return ResultUtil.success(rankList);
	}
	
	
	@PostMapping(value = "/taskattr/rankupdate")
	public Result<Map<String,Object>> taskAttrRankUpdate(@RequestBody @Valid TaskAttrParameter para,BindingResult bindingResult) {
		
		Map<String, Object> rankMap =  new HashMap<String, Object>();
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		
		if(para.getRankId()!= null && para.getRankId().length()>0 && para.getRankName()!=null)
		{
			Optional<TlGammaTaskAttrrank> optionalrank =  this.sysTaskService.getTaskRank(para.getRankId());
			if(optionalrank.isPresent())
			{
				TlGammaTaskAttrrank rank = optionalrank.get();
				rank.setRankName(para.getRankName());
				TlGammaTaskAttrrank tmp =this.sysTaskService.saveTaskAttrRank(rank);
				rankMap.put("rankId", tmp.getRankGuid());
				rankMap.put("rankName",tmp.getRankName());
			}
			
		}
		return ResultUtil.success(rankMap);
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
			if(attrParam.getIsEdit()!= null)
				taskattr.setAttrIsedit(attrParam.getIsEdit());
			else
				taskattr.setAttrIsedit(0);
			taskattr.setRankGuid(attrParam.getRankId());
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
			if(taskParam.getRefClassId() != null)
			{
				//将任务关联的核心对象及标签保存到数据库中
				Optional<TlGammaKernel> optionalkernel = this.sysKernelService.getByKernelClassid(taskParam.getRefClassId());
				if(optionalkernel.isPresent())
				{
					TlGammaTaskLayer taskLayer = new TlGammaTaskLayer();
					taskLayer.setLayerGuid(UUID.randomUUID().toString());
					taskLayer.setKernelClassid(taskParam.getRefClassId());
					taskLayer.setLayerName(optionalkernel.get().getKernelClassname());
					if(taskParam.getExtId()!= null)
					{
						Optional<TlGammaKernelExt> optionalext = this.sysKernelExtService.getExtById(taskParam.getExtId());
						if(optionalext.isPresent())
						{
							taskLayer.setExtGuid(taskParam.getExtId());
							taskLayer.setLayerName(optionalext.get().getExtName());
						}
					}
					taskLayer.setTaskGuid(taskParam.getTaskId());
					taskLayer.setLayerGrade(0);
					TlGammaTaskLayer tmpExt = this.sysTaskService.saveTaskLayer(taskLayer);
					
					fMap.put("refclassId", tmpExt.getKernelClassid());
					fMap.put("taskId", tmpExt.getTaskGuid());
				}
			}
		} catch(Exception e){
			throw new RuntimeException("error");
		}
		return ResultUtil.success(fMap);
	}
	
	@PostMapping(value = "/taskref/list")
	public Result<List<Map<String,Object>>> taskRefKernelList(@RequestBody @Valid TaskLayerParameter taskParam,BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		List<Map<String,Object>> resList = new LinkedList<Map<String ,Object>>();
		try
		{
			List<TlGammaTaskLayer> layerList = this.sysTaskService.getTaskLayerList(taskParam.getTaskId(),0);
			for(TlGammaTaskLayer layer : layerList)
			{
				Map<String,Object> layerMap = new HashMap<String,Object>();
				layerMap.put("layerId", layer.getLayerGuid());
				layerMap.put("layerName", layer.getLayerName());
				layerMap.put("kernelNum", layer.getKernelNum());
				resList.add(layerMap);
			}
		} catch(Exception e){
			throw new RuntimeException(e);
		}
		return ResultUtil.success(resList);
	}
	
	@PostMapping(value = "/taskref/delete")
	public Result<String> taskRefKerneldelete(@RequestBody @Valid TaskLayerParameter taskParam,BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		List<Map<String,Object>> resList = new LinkedList<Map<String ,Object>>();
		try
		{
			this.sysTaskService.deleteTaskLayer(taskParam.getLayerId());
			
		} catch(Exception e){
			throw new RuntimeException(e);
		}
		return ResultUtil.success("ok");
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
		String sortField ="updateTime";
		
		if (para.getSortDirection().equalsIgnoreCase("ASC")) {
			sort = new Sort(Direction.ASC, sortField);
		} else {
			sort = new Sort(Direction.DESC, sortField);
		}

		String filterStr ="";
		if(para.getSkey()!=null&&para.getSkey().length()>0)
		{
			filterStr = para.getSkey();
		}
		PageRequest pageInfo = new PageRequest(pageNumber, pageSize, sort);
		Page<TlGammaKernel> mList = this.sysKernelService.getGroupKernelList(cUser.getGroupGuid(),filterStr,pageInfo);
		for (TlGammaKernel eachKernel : mList) { 
			Map<String, Object> tMap = new HashMap<String, Object>();
			tMap.put("classId", eachKernel.getKernelClassid());
			tMap.put("className", eachKernel.getKernelClassname());
			tMap.put("descInfo", eachKernel.getKernelClassdesc());
			tMap.put("geoType",eachKernel.getGeomType());
			tMap.put("kernelnum","");
			tMap.put("tasknum","");
			list.add(tMap);
		}
		
		Page<Map<String, Object>> resPage = new PageImpl<Map<String, Object>>(list,pageInfo,mList.getTotalElements());
		return ResultUtil.success(resPage);
	}
	
	@PostMapping(value = "/kernel")
	public Result<Map<String,Object>> queryKernel(HttpServletRequest request ,@RequestBody @Valid GAParameter para) {
		
		Map<String,Object> result = new HashMap<String,Object>();
		Optional<TlGammaKernel> kernel = this.sysKernelService.getByKernelClassid(para.getId());
		if(!kernel.isPresent() )
		{
			return ResultUtil.error(400, ReturnDesc.KERNEL_IS_NOT_EXIST);
		}
		else
		{
			result.put("classId",kernel.get().getKernelClassid());
			result.put("className",kernel.get().getKernelClassname());
			result.put("descInfo",kernel.get().getKernelClassdesc());
			result.put("geoType",kernel.get().getGeomType());
		}
		List<TlGammaKernelAttr>  attrList = this.sysKernelService.getKernelAttrList(para.getId());
		
		List<Map<String,Object>> fieldList = attrList.stream().map(item ->{
			Map<String,Object> fieldMap = new HashMap<String,Object>();
			fieldMap.put("attrId", item.getAttrGuid());
			fieldMap.put("attrName",item.getAttrName());
			fieldMap.put("attrType",item.getAttrType());
			fieldMap.put("attrGrade",item.getAttrFgrade());
			fieldMap.put("attrDesc",item.getAttrDesc());
			fieldMap.put("minValue",item.getAttrMin());
			fieldMap.put("maxValue",item.getAttrMax());
			

			if(item.getDictId()!=null)
			{
				Optional<TlGammaDict> opDict = this.sysDictService.getDictbyId(item.getDictId());
				if(opDict.isPresent())
				{
					fieldMap.put("dictName",opDict.get().getDictName());
					fieldMap.put("dictId",item.getDictId());
				}
			}
			return fieldMap;
		}).collect(Collectors.toList());
		result.put("attrs", fieldList);
		return ResultUtil.success(result);
	}
	
	
	@PostMapping(value = "/kernel/attrs")
	public Result<Page<Map<String,Object>>> queryKernelAttrs(HttpServletRequest request ,@RequestBody RequestParameter para) {
		UserInfo cUser = this.utilService.getCurrentUser(request);
		int pageNumber = para.getPage();
		int pageSize = para.getPageSize();

		Sort sort = null;
		String sortField ="updateTime";
		
		if (para.getSortDirection().equalsIgnoreCase("ASC")) {
			sort = new Sort(Direction.ASC, sortField);
		} else {
			sort = new Sort(Direction.DESC, sortField);
		}

		String filterStr ="";
		if(para.getSkey()!=null&&para.getSkey().length()>0)
		{
			filterStr = para.getSkey();
		}
		PageRequest pageInfo = new PageRequest(pageNumber, pageSize, sort);
		Page<TlGammaKernelAttr>  attrList = this.sysKernelService.getKernelAttrList(para.getClassId(),cUser.getUserName(),filterStr,pageInfo);
		
		List<Map<String,Object>> fieldList = attrList.getContent().stream().map(item ->{
			Map<String,Object> fieldMap = new HashMap<String,Object>();
			fieldMap.put("attrId", item.getAttrGuid());
			fieldMap.put("attrName",item.getAttrName());
			fieldMap.put("attrType",item.getAttrType());
			fieldMap.put("attrGrade",item.getAttrFgrade());
			fieldMap.put("attrDesc",item.getAttrDesc());
			fieldMap.put("minValue",item.getAttrMin());
			fieldMap.put("maxValue",item.getAttrMax());
			if(item.getDictId()!=null)
			{
				Optional<TlGammaDict> opDict = this.sysDictService.getDictbyId(item.getDictId());
				if(opDict.isPresent())
				{
					fieldMap.put("dictName",opDict.get().getDictName());
					fieldMap.put("dictId",item.getDictId());
				}
			}
			return fieldMap;
		}).collect(Collectors.toList());
		Page<Map<String,Object>> resPage = new PageImpl<Map<String,Object>>(fieldList,pageInfo,attrList.getTotalElements());

		return ResultUtil.success(resPage);
	}
		
	
	@PostMapping(value = "/kernel/exttypes")
	public Result<Page<Map<String ,String >>> listKernelExttypes(HttpServletRequest request ,@RequestBody RequestParameter para, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}

		int pageNumber = para.getPage();
		int pageSize = para.getPageSize();

		Sort sort = null;
		String sortField ="updateTime";
		
		if (para.getSortDirection()!=null && para.getSortDirection().equalsIgnoreCase("ASC")) {
			sort = new Sort(Direction.ASC, sortField);
		} else {
			sort = new Sort(Direction.DESC, sortField);
		}

		String filterStr ="";
		if(para.getSkey()!=null&&para.getSkey().length()>0)
		{
			filterStr = para.getSkey();
		}
		PageRequest pageInfo = new PageRequest(pageNumber, pageSize, sort);
		
		UserInfo cUser = this.utilService.getCurrentUser(request);
		
		Page<Map<String ,String >> subList =  this.sysKernelExtService.getKernelExtList( filterStr, cUser.getUserName(),pageInfo);
		
		return ResultUtil.success(subList);
	}
	@PostMapping(value = "/kernel/exttype")
	public Result<Page<Map<String ,String >>> kernelExttype(@RequestBody GAParameter para) {
		
		Map<String ,String >  subType = new HashMap<String ,String>();
		if(para.getId()!= null && para.getId().length()>0)
		{
			Optional<TlGammaKernelExt> optionalExt =  this.sysKernelExtService.getExtById(para.getId());
			if(optionalExt.isPresent())
			{
				TlGammaKernelExt ext = optionalExt.get();
				subType.put("extDesc", ext.getExtDesc());
				subType.put("extId", ext.getExtGuid());
				subType.put("classId", ext.getKernelClassid());
				subType.put("extName", ext.getExtName());
				subType.put("extDesc", ext.getExtDesc());
				subType.put("kernelNum", String.valueOf(ext.getKernelNum()));
				subType.put("geoFilterNum", String.valueOf(ext.getGeofilterNum()));
				subType.put("filterNum", String.valueOf(ext.getFilterNum()));
			}
		}
		return ResultUtil.success(subType);
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
		ext.setExtName(para.getExtName());
		ext.setKernelClassid(para.getClassId());
		ext.setUserName(cUser.getUserName());
		ext.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		
		Optional<TlGammaUser> userOptional =  this.sysUserService.findOne(cUser.getUserName());
        if(userOptional.isPresent())
        {
        	if(userOptional.get().getRoleId().indexOf("ROLE_GROUP_ADMIN") >= 0)
        	{
        		ext.setExtGrade("GROUP");
        	}
        	else
        	{
        		ext.setExtGrade("USER");
        	}
        }
		TlGammaKernelExt tmp = this.sysKernelExtService.saveKernelExt(ext);
		rMap.put("extName", tmp.getExtName());
		rMap.put("extId", tmp.getExtGuid());
		return ResultUtil.success(rMap);
	}
	
	///更新核心对象子类
	@PostMapping(value = "/kernel/exttype/refresh")
	public Result<TlGammaKernelExt> updateExttypeKernels(HttpServletRequest request ,@RequestBody ExtTypeParameter extParam, BindingResult bindingResult) {
		Map<String,String> rMap = new HashMap<String,String>();
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}

		if(extParam.getExtId()!= null)
		{
			Optional<TlGammaKernelExt> kernelExt =  this.sysKernelExtService.getKernelExt(extParam.getExtId());
			if(kernelExt.isPresent())
			{
				TlGammaKernelExt ext = kernelExt.get();
				Optional<TlGammaKernel>  kernelOptional = this.sysKernelService.getByKernelClassid(ext.getKernelClassid());
				if(kernelOptional.isPresent())
				{
					List<TlGammaKernelFilter> filterList =  this.sysKernelExtService.getKernelExtFilter(ext.getExtGuid());
					List<TlGammaKernelGeoFilter> geofilterList = this.sysKernelExtService.getGeoFilter(ext.getExtGuid());
					GAExtParameter gaPara = new GAExtParameter();
					gaPara.setExtGuid(ext.getExtGuid());
					gaPara.setGeoType(kernelOptional.get().getGeomType());
					gaPara.setKernelClassid(ext.getKernelClassid());
					List<GAFilter> flist = new LinkedList<GAFilter>();
					if(filterList!= null && filterList.size()>0)
					{
						for(TlGammaKernelFilter filter:filterList)
						{
							GAFilter gaFilter= new GAFilter();
							Optional<TlGammaKernelAttr>  attr = this.sysKernelService.getAttrById(filter.getAttrGuid());
							gaFilter.setAttrGuid(filter.getAttrGuid());
							gaFilter.setAttrType(attr.get().getAttrType());
							gaFilter.setMinValue(filter.getMinValue());
							gaFilter.setMaxValue(filter.getMaxValue());
					        flist.add(gaFilter);
						}
					}
			        gaPara.setFilterList(flist);
			        
			        List<GAGeoFilter> geoList = new LinkedList<GAGeoFilter>();
			        if(geofilterList!= null && geofilterList.size()>0)
			        {
				        for(TlGammaKernelGeoFilter gfilter: geofilterList)
				        {
					        GAGeoFilter geofilter = new GAGeoFilter();
					        Map<String,Object> feaMap =new HashMap<String,Object>();
					        feaMap.put("type", "Feature");
					    	feaMap.put("geometry", JSONValue.parse(new GeometryJSON(10).toString(gfilter.getFilterGeom())));
					        geofilter.setFilterGeom(feaMap);
					        geofilter.setFilterGuid(gfilter.getFilterGuid());
					        geoList.add(geofilter);
				        }
			        }
			        gaPara.setGeofilterList(geoList);
			        HttpHeaders headers = new HttpHeaders();
		        	String serverPath = kernelOptional.get().getServerPath();
		        	if(serverPath== null || serverPath.length()<1)
		        	{
		        		String tmp = request.getRequestURL().toString();
		        		serverPath = tmp.substring(0,tmp.indexOf("service"));
		        		headers.add("x-auth-token", request.getHeader("x-auth-token") );
		        	}
					HttpEntity<GAExtParameter> entity = new HttpEntity<GAExtParameter>(gaPara,headers);
				    Result<List<Map<String,String>>> result = restTemplate.postForObject(serverPath+"ga/kernel/exttype/refresh",entity,Result.class);
				}
			}
		}
		return ResultUtil.error(400, ReturnDesc.UNKNOW_ERROR);
	}
	
	///更新核心对象子类基本信息
	@PostMapping(value = "/kernel/exttype/update")
	public Result<TlGammaKernelExt> updateKernelExttype(HttpServletRequest request ,@RequestBody ExtTypeParameter para, BindingResult bindingResult) {
		Map<String,String> rMap = new HashMap<String,String>();
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}

		if(para.getExtId()!= null)
		{
			Optional<TlGammaKernelExt> kernelExt =  this.sysKernelExtService.getKernelExt(para.getExtId());
			if(kernelExt.isPresent())
			{
				TlGammaKernelExt tmpExt = kernelExt.get();
				if(para.getExtName()!= null)
					tmpExt.setExtName(para.getExtName());
				if(para.getExtDesc()!= null)
					tmpExt.setExtDesc(para.getExtDesc());
				tmpExt.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				TlGammaKernelExt saveExt = this.sysKernelExtService.saveKernelExt(tmpExt);
				rMap.put("extName", saveExt.getExtName());
				rMap.put("extId", saveExt.getExtGuid());
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
			nFilter.setExtGuid(para.getExtId());
			nFilter.setAttrGuid(para.getAttrId());
			nFilter.setMaxValue(para.getMaxValue());
			nFilter.setMinValue(para.getMinValue());
			nFilter.setEnumStr(para.getEnumStr());
			nFilter.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			
			TlGammaKernelFilter saveFilter = this.sysKernelExtService.saveFilter(nFilter);
			rMap.put("filterId", saveFilter.getFilterGuid());
			rMap.put("extId", saveFilter.getExtGuid());
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
	public Result<Map<String,String>> addGeoFilterToKernelExttype(HttpServletRequest request ,@RequestBody GeoFilterParameter para, BindingResult bindingResult) {
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
				geoFilter.setExtGuid(para.getExtId());
				geoFilter.setFilterGuid(UUID.randomUUID().toString());
				geoFilter.setFilterGeom((Polygon) filterGeom);
				geoFilter.setFilterName(para.getFilterName());
				TlGammaKernelGeoFilter saveFilter = this.sysKernelExtService.saveGeoFilter(geoFilter);
				rMap.put("filterId", saveFilter.getFilterGuid());
				rMap.put("extId", saveFilter.getExtGuid());
				return ResultUtil.success(rMap);
			}
		}
		return ResultUtil.error(400, ReturnDesc.UNKNOW_ERROR);
	}
	
	@PostMapping(value = "/kernel/exttype/updategeofilter")
	public Result<String> updateGeoFilterToKernelExttype(HttpServletRequest request ,@RequestBody GeoFilterParameter para, BindingResult bindingResult) {
		Map<String,String> rMap = new HashMap<String,String>();
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}

		Geometry filterGeom = null;
		try {
			if(para.getFilterGeom()!=null)
			{
				String rangeStr = JSONValue.toJSONString(para.getFilterGeom());
				filterGeom = new GeometryJSON(10).read(rangeStr);
				filterGeom.setSRID(4326);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			filterGeom = null;
			e.printStackTrace();
		}
		
		if(para.getFilterId()!= null)
		{
			Optional<TlGammaKernelGeoFilter> geoFilterOptional = this.sysKernelExtService.getGeoFilterByFilterId(para.getFilterId());
			if(geoFilterOptional.isPresent())
			{
				TlGammaKernelGeoFilter geoFilter = geoFilterOptional.get();
				if(filterGeom!= null)
					geoFilter.setFilterGeom((Polygon) filterGeom);
				if(para.getFilterName()!= null)
					geoFilter.setFilterName(para.getFilterName());
				TlGammaKernelGeoFilter saveFilter = this.sysKernelExtService.saveGeoFilter(geoFilter);
				rMap.put("filterId", saveFilter.getFilterGuid());
				rMap.put("extId", saveFilter.getExtGuid());
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
	
	
	@PostMapping(value = "/kernel/exttype/filterlist")
	public Result<List<Map<String,String>>> kernelExttypeFilterList(HttpServletRequest request ,@RequestBody ExtTypeParameter extParam, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		List<Map<String,String>> list = new LinkedList<Map<String,String>>();
		
		List<TlGammaKernelFilter> filterList = this.sysKernelExtService.getFilter(extParam.getExtId());
		for(TlGammaKernelFilter filter : filterList)
		{
			Optional<TlGammaKernelAttr> kernelAttr = this.sysKernelService.getAttrById(filter.getAttrGuid());
			if(kernelAttr.isPresent())
			{
				Map<String,String> filterMap = new HashMap<String,String>();
				filterMap.put("filterId", filter.getFilterGuid());
				filterMap.put("attrId", filter.getAttrGuid());
				filterMap.put("attrName", kernelAttr.get().getAttrName());
				filterMap.put("attrType", kernelAttr.get().getAttrType());
				filterMap.put("maxValue", filter.getMaxValue());
				filterMap.put("minValue", filter.getMinValue());
				filterMap.put("enumStr", filter.getEnumStr());
				list.add(filterMap);
			}
		}
		return ResultUtil.success(list);
	}
	
	@PostMapping(value = "/kernel/exttype/geofilterlist")
	public Result<List<Map<String,Object>>> kernelExttypeGeoFilterList(HttpServletRequest request ,@RequestBody ExtTypeParameter extParam, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		List<Map<String,Object>> list = new LinkedList<Map<String,Object>>();
		
		List<TlGammaKernelGeoFilter> filterList = this.sysKernelExtService.getGeoFilter(extParam.getExtId());
		for(TlGammaKernelGeoFilter filter : filterList)
		{
				Map<String,Object> filterMap = new HashMap<String,Object>();
				filterMap.put("filterId", filter.getFilterGuid());
				filterMap.put("filterName", filter.getFilterName());
				filterMap.put("geometry", JSONValue.parse(new GeometryJSON(10).toString(filter.getFilterGeom())));
				list.add(filterMap);
		}
		return ResultUtil.success(list);
	}
	
	@PostMapping(value = "/kernel/exttype/delete")
	public Result<TlGammaKernelExt> kernelExttype(HttpServletRequest request ,@RequestBody ExtTypeParameter extParam, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}

		this.sysKernelExtService.deleteKernelExt(extParam.getExtId());
		//删除核心对象所在节点的标签
		//TODO
		if(extParam.getExtId()!= null)
		{
			GAExtParameter gaPara = new GAExtParameter();
			gaPara.setExtGuid(extParam.getExtId());
			Optional<TlGammaKernelExt> kernelExt =  this.sysKernelExtService.getKernelExt(extParam.getExtId());
			if(kernelExt.isPresent())
			{
				TlGammaKernelExt ext = kernelExt.get();
				Optional<TlGammaKernel>  kernelOptional = this.sysKernelService.getByKernelClassid(ext.getKernelClassid());
				if(kernelOptional.isPresent())
				{
					HttpHeaders headers = new HttpHeaders();
			    	String serverPath = kernelOptional.get().getServerPath();
			    	if(serverPath== null || serverPath.length()<1)
			    	{
			    		String tmp = request.getRequestURL().toString();
			    		serverPath = tmp.substring(0,tmp.indexOf("service"));
			    		headers.add("x-auth-token", request.getHeader("x-auth-token") );
			    	}
					HttpEntity<GAExtParameter> entity = new HttpEntity<GAExtParameter>(gaPara,headers);
				    Result<List<Map<String,String>>> result = restTemplate.postForObject(serverPath+"ga/kernel/exttype/remove",entity,Result.class);
				}
			}
		}
	   
		return ResultUtil.success(ReturnDesc.EXECUTION_SUCCESS);
	}
}
