package com.cellinfo.controller;

import java.util.UUID;

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
import com.cellinfo.controller.entity.RequestParameter;
import com.cellinfo.entity.Result;
import com.cellinfo.entity.TlGammaTask;
import com.cellinfo.service.SysTaskService;
import com.cellinfo.util.ReturnDesc;
import com.cellinfo.utils.ResultUtil;

@ServiceLog(moduleName = "任务操作")
@PreAuthorize("hasRole('ROLE_ADMIN')")  
@RestController
@RequestMapping("/service/task")
public class SysTaskController {

	private final static Logger logger = LoggerFactory.getLogger(SysTaskController.class);
	
	@Autowired
	private SysTaskService sysTaskService ;
	
	
	@PostMapping(value = "/tasks")
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
	
	@PostMapping(value = "/add")
	public Result<TlGammaTask> addGroup(@RequestBody @Valid TlGammaTask task, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		Iterable<TlGammaTask> tasklist = this.sysTaskService.getByName(task.getTaskName());
		if(tasklist!=null && tasklist.iterator().hasNext())
		{
			return ResultUtil.error(400, ReturnDesc.GROUP_NAME_IS_EXIST);
		}
		task.setTaskGuid(UUID.randomUUID().toString());
		return ResultUtil.success(this.sysTaskService.addTask(task));
	}
	
	@PostMapping(value = "/update")
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
	
	@PostMapping(value = "/delete")
	public Result<String> updateGroup(@RequestBody @Valid String taskGuid) {

		this.sysTaskService.deleteTask(taskGuid);
		
		return ResultUtil.success("success");
	}

	
}
