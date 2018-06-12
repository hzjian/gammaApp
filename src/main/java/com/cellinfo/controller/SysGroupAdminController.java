package com.cellinfo.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cellinfo.annotation.ServiceLog;
import com.cellinfo.controller.entity.GAParameter;
import com.cellinfo.controller.entity.KernelParameter;
import com.cellinfo.controller.entity.RequestParameter;
import com.cellinfo.controller.entity.UserParameter;
import com.cellinfo.entity.Result;
import com.cellinfo.entity.TlGammaGroup;
import com.cellinfo.entity.TlGammaKernel;
import com.cellinfo.entity.TlGammaTask;
import com.cellinfo.entity.TlGammaUser;
import com.cellinfo.entity.ViewTaskUser;
import com.cellinfo.security.UserInfo;
import com.cellinfo.service.SysGroupService;
import com.cellinfo.service.SysKernelService;
import com.cellinfo.service.SysTaskService;
import com.cellinfo.service.SysUserService;
import com.cellinfo.service.UtilService;
import com.cellinfo.utils.ResultUtil;
import com.cellinfo.utils.ReturnDesc;

/**
 *  组织成员(本地)
 *  核心数据(远端)
 *  数据分类(远端)
 *  组织数据字典管理(本地)
 *  任务查看
 * @author zhangjian
 *
 */
@ServiceLog(moduleName = "组织管理模块")
@PreAuthorize("hasRole('ROLE_GROUP_ADMIN')")  
@RestController
@RequestMapping("/service/group")
public class SysGroupAdminController {

	private final static Logger logger = LoggerFactory.getLogger(SysGroupAdminController.class);
	
	@Autowired
	private UtilService utilService;
	
	@Autowired
	private SysUserService sysUserService;
	
	@Autowired
	private SysKernelService sysKernelService;
	
	@Autowired
	private SysTaskService sysTaskService;
	
	@Autowired
	private SysGroupService sysGroupService;
	
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");   
	
	private BCryptPasswordEncoder  encoder =new BCryptPasswordEncoder();
	
	private final static String DEFAULT_PASSWORD = "PASSWORD";
	
	@PostMapping(value = "/kernel/save")
	public Result<Map<String,String>> addKernel(HttpServletRequest request ,@RequestBody @Valid KernelParameter kernel, BindingResult bindingResult) {
		UserInfo cUser = this.utilService.getCurrentUser(request);
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		TlGammaKernel tmpKernel = new TlGammaKernel();

		tmpKernel.setKernelClassid(UUID.randomUUID().toString());
		tmpKernel.setGroupGuid(cUser.getGroupGuid());
		tmpKernel.setKernelClassdesc(kernel.getDescInfo());
		tmpKernel.setKernelClassname(kernel.getClassName());
		tmpKernel.setGeomType(kernel.getGeomType());
		tmpKernel.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		
		List<TlGammaKernel> kernellist = this.sysKernelService.getByKernelClassname(tmpKernel.getKernelClassname());
		if(kernellist!=null && kernellist.iterator().hasNext())
		{
			return ResultUtil.error(400, ReturnDesc.KERNEL_NAME_IS_EXIST);
		}
		
		Map<String,String> result = new HashMap<String,String>();
		TlGammaKernel saveKernel = this.sysKernelService.addGroupKernel(tmpKernel);
		result.put("classId", saveKernel.getKernelClassid());
		result.put("className", saveKernel.getKernelClassname());
		return ResultUtil.success(result);
	}
	
	
	
	@PostMapping(value = "/kernel/update")
	public Result<Map<String,String>> updateKernel(HttpServletRequest request ,@RequestBody @Valid KernelParameter kernel, BindingResult bindingResult) {
		UserInfo cUser = this.utilService.getCurrentUser(request);
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		Optional<TlGammaKernel> tmpKernelOptional = this.sysKernelService.getByKernelClassid(kernel.getClassId());
		if(!tmpKernelOptional.isPresent() )
		{
			return ResultUtil.error(400, ReturnDesc.KERNEL_IS_NOT_EXIST);
		}
		TlGammaKernel tmpKernel =tmpKernelOptional.get();
		if(kernel.getDescInfo()!=null)
			tmpKernel.setKernelClassdesc(kernel.getDescInfo());
		if(kernel.getClassName()!=null)
		{
			List<TlGammaKernel> kernellist = this.sysKernelService.getByKernelClassname(kernel.getClassName());
			if(kernellist!=null && kernellist.iterator().hasNext())
			{
				return ResultUtil.error(400, ReturnDesc.KERNEL_NAME_IS_EXIST);
			}
			tmpKernel.setKernelClassname(kernel.getClassName());
		}
		if(kernel.getGeomType()!=null)
			tmpKernel.setGeomType(kernel.getGeomType());

		tmpKernel.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		Map<String,String> result = new HashMap<String,String>();
		TlGammaKernel saveKernel = this.sysKernelService.addGroupKernel(tmpKernel);
		
		result.put("classId", saveKernel.getKernelClassid());
		result.put("className", saveKernel.getKernelClassname());
		
		return ResultUtil.success(result);
	}
	
	
	@PostMapping(value = "/kernel/delete")
	public Result<String> updateKernel(@RequestBody @Valid String kernelGuid) {

		return ResultUtil.success(this.sysKernelService.deleteGroupKernel(kernelGuid));
	}

	//group member 
	@PostMapping(value = "/members")
	public Result<Page<Map<String, Object>>> groupMemberList(HttpServletRequest request,@RequestBody RequestParameter para, BindingResult bindingResult) {
		List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
		UserInfo cUser = this.utilService.getCurrentUser(request);
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
		Page<TlGammaUser> mList = this.sysUserService.getGroupMember(pageInfo,filterStr,cUser.getGroupGuid());
		for (TlGammaUser eachUser : mList) {
			Map<String, Object> tMap = new HashMap<String, Object>();
			tMap.put("userName", eachUser.getUserName());
			tMap.put("userCnname", eachUser.getUserCnname());
			tMap.put("userEmail",eachUser.getUserEmail());
			tMap.put("userStatus", eachUser.getAccountStatus());
			if(eachUser.getLoginTime()!= null)
				tMap.put("lastLoginTime", df.format(eachUser.getLoginTime()));
			list.add(tMap);
		}
		Page<Map<String, Object>> resPage = new PageImpl<Map<String, Object>>(list,pageInfo,mList.getTotalElements());
		return ResultUtil.success(resPage);
	}
	
	@PostMapping(value = "/member")
	public Result<Map<String,Object>> adminUser(@RequestBody GAParameter para, BindingResult bindingResult) {
		Map<String,Object> tmp = new HashMap<String,Object>();
		if(para.getId()!= null)
		{
			TlGammaUser tmpUser = this.sysUserService.getGroupMemberUser(para.getId());
			if(tmpUser!= null)
			{
				tmp.put("groupId", tmpUser.getGroupGuid());
				tmp.put("userCnname",tmpUser.getUserCnname());
				if(tmpUser.getGroupGuid()!=null)
				{
					Optional<TlGammaGroup> group = this.sysGroupService.findOne(tmpUser.getGroupGuid());
					if(group.isPresent())
						tmp.put("groupName",group.get().getGroupName());
				}
				tmp.put("userName",tmpUser.getUsername());
				tmp.put("userStatus", tmpUser.getAccountStatus());
				tmp.put("userEmail", tmpUser.getUserEmail());
				if(tmpUser.getLoginTime()!= null)
					tmp.put("lastLoginTime", df.format(tmpUser.getLoginTime()));
			}
		}
		
		return ResultUtil.success(tmp);
	}
	@PostMapping(value = "/member/testname")
	public Result<TlGammaUser> testGroupMemberName(HttpServletRequest request,@RequestBody @Valid String membername, BindingResult bindingResult) {	
		Optional<TlGammaUser> kuser = this.sysUserService.findOne(membername);
		if(kuser.isPresent())
		{
			return ResultUtil.error(400, ReturnDesc.KERNEL_NAME_IS_EXIST);
		}
		return ResultUtil.success(membername);
	}
	
	@PostMapping(value = "/member/save")
	public Result<Map<String, String>> saveGroupMember(HttpServletRequest request,@RequestBody @Valid UserParameter member, BindingResult bindingResult) {
		Map<String, String> tMap = new HashMap<String, String>();
		UserInfo cUser = this.utilService.getCurrentUser(request);
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		Optional<TlGammaUser> kuserOptional = this.sysUserService.findOne(member.getUserName());
		if(kuserOptional.isPresent())
			return ResultUtil.error(400, ReturnDesc.USER_NAME_IS_EXIST);
		
		TlGammaUser tmpUser = new TlGammaUser();

		tmpUser.setUserGuid(UUID.randomUUID().toString());
		tmpUser.setGroupGuid(cUser.getGroupGuid());
		tmpUser.setUserEmail(member.getUserEmail());
		tmpUser.setRoleId("ROLE_USER");
		tmpUser.setUserCnname(member.getUserCnname());
		tmpUser.setUserName(member.getUserName());
		tmpUser.setUserPassword(encoder.encode(member.getUserPassword()));
		tmpUser.setAccountEnabled(true);
		tmpUser.setAccountNonExpired(true);
		tmpUser.setAccountNonLocked(true);
		tmpUser.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		TlGammaUser saveResult = this.sysUserService.save(tmpUser);	
		tMap.put("userName", saveResult.getUsername());
		tMap.put("userEmail", saveResult.getUserEmail());
		return ResultUtil.success(tMap);
	}
	
	@PostMapping(value = "/member/update")
	public Result<Map<String, String>> updateGroupMember(@RequestBody @Valid UserParameter member, BindingResult bindingResult) {
		Map<String, String> tMap = new HashMap<String, String>();
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		Optional<TlGammaUser> kuserOptional = this.sysUserService.findOne(member.getUserName());
		if(!kuserOptional.isPresent())
			return ResultUtil.error(400, ReturnDesc.USER_NAME_IS_NOT_EXIST);
		
		TlGammaUser kuser = kuserOptional.get();
		if(member.getUserCnname()!=null)
			kuser.setUserCnname(member.getUserCnname());
		if(member.getUserEmail()!=null)
			kuser.setUserEmail(member.getUserEmail());
		if(member.getUserPassword()!=null && !member.getUserPassword().equalsIgnoreCase(DEFAULT_PASSWORD))
			kuser.setUserPassword(encoder.encode(member.getUserPassword()));	
		if(member.getUserStatus()!=null)
			kuser.setAccountEnabled(member.getUserStatus()==1?true:false);

		kuser.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		TlGammaUser saveResult = this.sysUserService.save(kuser);	
		tMap.put("userName", saveResult.getUsername());
		tMap.put("userEmail", saveResult.getUserEmail());
		return ResultUtil.success(tMap);
	}
	
	@PostMapping(value = "/member/delete")
	public Result<String> deleteGroupMember(@RequestBody @Valid String username) {
		this.sysUserService.delete(username);
		return ResultUtil.success(ReturnDesc.EXECUTION_SUCCESS);
	}
	
	//组织任务查看
	@PostMapping(value = "/tasklist")
	public Result<Page<Map<String, Object>>>  groupTaskList (HttpServletRequest request,@RequestBody @Valid RequestParameter para, BindingResult bindingResult) 
	{
		List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
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
		Page<TlGammaTask> mList = this.sysTaskService.getTaskByGroupGuid(cUser.getGroupGuid(),pageInfo);
		for (TlGammaTask eachTask : mList) {
			Map<String, Object> tMap = new HashMap<String, Object>(); 
			tMap.put("taskId", eachTask.getTaskGuid());
			tMap.put("taskName", eachTask.getTaskName());
			if(eachTask.getStartTime()!= null)
				tMap.put("startTime", df.format(eachTask.getStartTime()));
			if(eachTask.getTerminalTime()!= null)
				tMap.put("terminalTime", df.format(eachTask.getTerminalTime()));
			tMap.put("userName", eachTask.getUserName());
			list.add(tMap);
		}
		Page<Map<String, Object>> resPage = new PageImpl<Map<String, Object>>(list,pageInfo,mList.getTotalElements());
		return ResultUtil.success(resPage);
	}
	
	//组织用户创建任务查看
	@PostMapping(value = "/usertasklist")
	public Result<Page<Map<String, Object>>>  userTaskList(HttpServletRequest request,@RequestBody @Valid RequestParameter para, BindingResult bindingResult) 
	{
		List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
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
		Page<TlGammaTask> mList = this.sysTaskService.getTaskByUsername(para.getSkey(),pageInfo);
		for (TlGammaTask eachTask : mList) {
			Map<String, Object> tMap = new HashMap<String, Object>(); 
			tMap.put("taskId", eachTask.getTaskGuid());
			tMap.put("taskName", eachTask.getTaskName());
			if(eachTask.getStartTime()!= null)
				tMap.put("startTime", df.format(eachTask.getStartTime()));
			if(eachTask.getTerminalTime()!= null)
				tMap.put("terminalTime", df.format(eachTask.getTerminalTime()));
			tMap.put("userName", eachTask.getUserName());
			list.add(tMap);
		}
		Page<Map<String, Object>> resPage = new PageImpl<Map<String, Object>>(list,pageInfo,mList.getTotalElements());
		return ResultUtil.success(resPage);
	}
	
	//组织用户参与任务查看
	// TODO
	@PostMapping(value = "/parttasklist")
	public Result<Page<Map<String, Object>>>  partTaskList(HttpServletRequest request,@RequestBody @Valid RequestParameter para, BindingResult bindingResult) 
	{
		List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
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
		Page<ViewTaskUser> mList = this.sysTaskService.getTaskByUserParticapate(para.getSkey(),pageInfo);
		for (ViewTaskUser eachTask : mList) {
			Map<String, Object> tMap = new HashMap<String, Object>(); 
			tMap.put("taskId", eachTask.getId().getTaskGuid());
			tMap.put("taskName", eachTask.getTaskName());
			if(eachTask.getStartTime()!= null)
				tMap.put("startTime", df.format(eachTask.getStartTime()));
			if(eachTask.getTerminalTime()!= null)
				tMap.put("terminalTime", df.format(eachTask.getTerminalTime()));
			tMap.put("userName", eachTask.getId().getUserName());
			list.add(tMap);
		}
		Page<Map<String, Object>> resPage = new PageImpl<Map<String, Object>>(list,pageInfo,mList.getTotalElements());
		return ResultUtil.success(resPage);
	}
}
