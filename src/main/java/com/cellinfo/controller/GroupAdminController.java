package com.cellinfo.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import com.cellinfo.controller.entity.KernelField;
import com.cellinfo.controller.entity.RequestParameter;
import com.cellinfo.controller.entity.UserParameter;
import com.cellinfo.entity.Result;
import com.cellinfo.entity.TlGammaGroup;
import com.cellinfo.entity.TlGammaKernel;
import com.cellinfo.entity.TlGammaUser;
import com.cellinfo.security.UserInfo;
import com.cellinfo.service.SysGroupService;
import com.cellinfo.service.SysKernelService;
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
@ServiceLog(moduleName = "组织管理接口")
@PreAuthorize("hasRole('ROLE_GROUP_ADMIN')")  
@RestController
@RequestMapping("/service/group")
public class GroupAdminController {

	private final static Logger logger = LoggerFactory.getLogger(GroupAdminController.class);
	
	@Autowired
	private UtilService utilService;
	
	@Autowired
	private SysUserService sysUserService;
	
	@Autowired
	private SysGroupService sysGroupService;
	
	@Autowired
	private SysKernelService sysKernelService;
	
	private BCryptPasswordEncoder  encoder =new BCryptPasswordEncoder();
	
	@PostMapping(value = "/curUserInfo")
	public Result<Map<String, String>> getCurrentUserInfo(HttpServletRequest request) {
		Map<String, String> tMap = new HashMap<String, String>();
		//String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
		
		UserInfo currentUser = this.utilService.getCurrentUser(request);
		if (currentUser!=null && currentUser.getUserName().length()>0) {

			logger.info("currentUser=="+currentUser);
			TlGammaUser user = this.sysUserService.findOne(currentUser.getUserName());
			logger.info("user=="+user);
			tMap.put("username", user.getUsername());
			tMap.put("usercnname", user.getUserCnname());
			
			return ResultUtil.success(tMap);
			
        } 
		return ResultUtil.error(400, ReturnDesc.USER_INFO_IS_ILLEGAL);
	}

	
	
	@PostMapping(value = "/kernels")
	public Result<List<Map<String, Object>>> kernelList(@RequestBody RequestParameter para, BindingResult bindingResult) {
		List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
		
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

		PageRequest pageInfo = new PageRequest(pageNumber, pageSize, sort);
		Page<TlGammaKernel> mList = this.sysKernelService.getGroupKernelList(pageInfo);
		for (TlGammaKernel eachKernel : mList) {
			Map<String, Object> tMap = new HashMap<String, Object>();
			tMap.put("key", eachKernel.getKernelClassid());
			tMap.put("name", eachKernel.getKernelClassname());
			tMap.put("descinfo", eachKernel.getKernelClassdesc());
			list.add(tMap);
		}
		
		return ResultUtil.success(list);
	}
	
	@PostMapping(value = "/kernel/save")
	public Result<TlGammaKernel> addKernel(@RequestBody @Valid KernelField kernel, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		TlGammaKernel tmpKernel = new TlGammaKernel();
		if(kernel.getKey().trim().equalsIgnoreCase("0"))
			tmpKernel.setKernelClassid(UUID.randomUUID().toString());
		tmpKernel.setGroupGuid("");
		tmpKernel.setKernelClassdesc(kernel.getDescinfo());
		tmpKernel.setKernelClassname(kernel.getName());
		
		Iterable<TlGammaGroup> kernellist = this.sysGroupService.getByName(tmpKernel.getKernelClassname());
		if(kernellist!=null && kernellist.iterator().hasNext())
		{
			return ResultUtil.error(400, ReturnDesc.KERNEL_NAME_IS_EXIST);
		}
		return ResultUtil.success(this.sysKernelService.addGroupKernel(tmpKernel));
	}
	
	@PostMapping(value = "/kernel/update")
	public Result<TlGammaKernel> updateKernel(@RequestBody @Valid TlGammaKernel kernel, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		TlGammaKernel refKernel = this.sysKernelService.getByKernelClassid(kernel.getKernelClassid());
		if(refKernel!=null && refKernel.getKernelClassid().length()>1)
		{
			return ResultUtil.success(this.sysKernelService.updateGroupKernel(kernel));
		}
		return ResultUtil.error(400, ReturnDesc.GROUP_NAME_IS_NOT_EXIST);
	}
	
	@PostMapping(value = "/kernel/delete")
	public Result<String> updateKernel(@RequestBody @Valid String kernelGuid) {

		return ResultUtil.success(this.sysKernelService.deleteGroupKernel(kernelGuid));
	}
	
	//group member 
	@PostMapping(value = "/members")
	public Result<List<Map<String, Object>>> groupMemberList(HttpServletRequest request,@RequestBody RequestParameter para, BindingResult bindingResult) {
		List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
		UserInfo cUser = this.utilService.getCurrentUser(request);
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		int pageNumber = para.getPage();
		int pageSize = para.getPageSize();

		Sort sort = null;
		String sortField ="userName";
		
		if (para.getSortDirection().equalsIgnoreCase("ASC")) {
			sort = new Sort(Direction.ASC, sortField);
		} else {
			sort = new Sort(Direction.DESC, sortField);
		}

		
		PageRequest pageInfo = new PageRequest(pageNumber, pageSize, sort);
		Page<TlGammaUser> mList = this.sysUserService.getGroupMember(pageInfo,cUser.getGroupGuid());
		for (TlGammaUser eachUser : mList) {
			Map<String, Object> tMap = new HashMap<String, Object>();
			tMap.put("userGuid", eachUser.getUserGuid());
			tMap.put("userName", eachUser.getUserName());
			tMap.put("userCnname", eachUser.getUserCnname());
			tMap.put("password", "*********");
			list.add(tMap);
		}
		
		return ResultUtil.success(list);
	}
	
	@PostMapping(value = "/member/save")
	public Result<TlGammaKernel> saveGroupMember(HttpServletRequest request,@RequestBody @Valid UserParameter member, BindingResult bindingResult) {
		UserInfo cUser = this.utilService.getCurrentUser(request);
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		TlGammaUser tmpUser = new TlGammaUser();
		if(member.getUserGuid().trim().equalsIgnoreCase("0"))
			tmpUser.setUserGuid(UUID.randomUUID().toString());
		tmpUser.setGroupGuid(cUser.getGroupGuid());
		tmpUser.setRoleId("ROLE_USER");
		tmpUser.setUserCnname(member.getUserCnname());
		tmpUser.setUserName(member.getUserName());
		tmpUser.setUserPassword(member.getUserPassword());

		TlGammaUser realUser = this.sysUserService.findOne(member.getUserName());

		if(! tmpUser.getUserPassword().equalsIgnoreCase(realUser.getUserPassword()) )
			realUser.setUserPassword(encoder.encode(tmpUser.getUserPassword()));
		
		
		TlGammaUser kuser = this.sysUserService.findOne(tmpUser.getUserName());
		if(kuser!=null )
		{
			return ResultUtil.error(400, ReturnDesc.KERNEL_NAME_IS_EXIST);
		}
		return ResultUtil.success(this.sysUserService.save(tmpUser));
	}
	
	@PostMapping(value = "/member/update")
	public Result<TlGammaKernel> updateGroupMember(@RequestBody @Valid TlGammaKernel kernel, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		TlGammaKernel refKernel = this.sysKernelService.getByKernelClassid(kernel.getKernelClassid());
		if(refKernel!=null && refKernel.getKernelClassid().length()>1)
		{
			return ResultUtil.success(this.sysKernelService.updateGroupKernel(kernel));
		}
		return ResultUtil.error(400, ReturnDesc.GROUP_NAME_IS_NOT_EXIST);
	}
	
	@PostMapping(value = "/member/delete")
	public Result<String> deleteGroupMember(@RequestBody @Valid String kernelGuid) {

		return ResultUtil.success(this.sysKernelService.deleteGroupKernel(kernelGuid));
	}
	
	//组织任务查看
	
	public void  groupTaskList ()
	{
		
	}
	
	//组织用户相关任务查看
	public void userTaskList()
	{
		
	}
}
