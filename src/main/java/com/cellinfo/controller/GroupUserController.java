package com.cellinfo.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.cellinfo.controller.entity.RequestParameter;
import com.cellinfo.controller.entity.TaskParameter;
import com.cellinfo.controller.entity.UserParameter;
import com.cellinfo.entity.Result;
import com.cellinfo.entity.TlGammaKernel;
import com.cellinfo.entity.TlGammaTask;
import com.cellinfo.entity.TlGammaTaskUser;
import com.cellinfo.entity.TlGammaUser;
import com.cellinfo.security.UserInfo;
import com.cellinfo.service.SysGroupService;
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
	private SysGroupService sysGroupService;
	
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
			tpara.setEnddatestr(df.format(task.getTaskTimeend()));
			tpara.setStartdatestr(df.format(task.getTaskTimestart()));
			tpara.setTaskGuid(task.getTaskGuid());
			tpara.setTaskname(task.getTaskName());
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
		TlGammaTask realTask = this.sysTaskService.getByGUID(taskGuid);
		TaskParameter tpara = new TaskParameter();

		tpara.setEnddatestr(df.format(realTask.getTaskTimeend()));
		tpara.setStartdatestr(df.format(realTask.getTaskTimestart()));
		tpara.setTaskGuid(realTask.getTaskGuid());
		tpara.setTaskname(realTask.getTaskName());


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
			uMap.put("username", user.getUserName());
			uMap.put("userguid",user.getUserGuid());
			userData.add(uMap);
		}
		
		List<Map<String ,String>> kernelData = new LinkedList<Map<String,String>>();
		for(TlGammaKernel kernel : kernelClassList) {
			Map<String ,String > kMap = new HashMap<String,String>();
			kMap.put("classname",kernel.getKernelClassname());
			kMap.put("classid",kernel.getKernelClassid());
			kernelData.add(kMap);
		}
		resList.put("userlist", userData);
		resList.put("kernellist", kernelData);

		return ResultUtil.success(resList);
	}


	@Transactional
	@PostMapping(value = "/task/create")
	public Result<Map<String,Object>> createTask(HttpServletRequest request,
			@RequestBody @Valid TaskParameter taskParam, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		Map<String,Object> resList = new HashMap<String ,Object>();
		
		UserInfo cUser = this.utilService.getCurrentUser(request);
		
		try {
			TlGammaTask task = new TlGammaTask();
			String taskGuid = UUID.randomUUID().toString();
			task.setTaskGuid(taskGuid);
			task.setTaskName(taskParam.getTaskname());
			task.setTaskTimestart( Timestamp.valueOf(taskParam.getStartdatestr()));
			task.setTaskTimeend(Timestamp.valueOf(taskParam.getEnddatestr()));
			if(taskParam.getKernellist()!=null && taskParam.getKernellist().size()>0)
			{
				task.setKernelClassid(taskParam.getKernellist().get(0).getClassGuid());
			}
			
			this.sysTaskService.addTask(task);
			
			if(taskParam.getFieldlist()!=null && taskParam.getFieldlist().size()>0) {
				for(FieldParameter field : taskParam.getFieldlist()) {
					
				}
			}
			
			if(taskParam.getUserlist()!= null && taskParam.getUserlist().size()>0) {
				for(UserParameter user : taskParam.getUserlist()) {
					TlGammaTaskUser taskUser = new TlGammaTaskUser();
					taskUser.setTaskGuid(taskGuid);
					taskUser.setUserGuid(user.getUserGuid());
					this.sysTaskService.saveTaskUser(taskUser);
				}
			}
		} catch(Exception e){
			throw new RuntimeException("error");
		}

		return ResultUtil.success(resList);
	}
	
	@PostMapping(value = "/task/update")
	public Result<TlGammaTask> updateGroup(@RequestBody @Valid TlGammaTask task, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		TlGammaTask refTask = this.sysTaskService.getByGUID(task.getTaskGuid());
		if(refTask!=null && refTask.getGroupGuid().length()>1)
		{
			return ResultUtil.success(this.sysTaskService.updateTask(task));
		}
		return ResultUtil.error(400, ReturnDesc.GROUP_NAME_IS_NOT_EXIST);
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
}
