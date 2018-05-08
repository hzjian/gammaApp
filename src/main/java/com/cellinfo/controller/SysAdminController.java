package com.cellinfo.controller;

import java.util.HashMap;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cellinfo.annotation.ServiceLog;
import com.cellinfo.controller.entity.RequestParameter;
import com.cellinfo.controller.entity.UserParameter;
import com.cellinfo.entity.Result;
import com.cellinfo.entity.TlGammaGroup;
import com.cellinfo.entity.TlGammaUser;
import com.cellinfo.security.UserInfo;
import com.cellinfo.service.SysGroupService;
import com.cellinfo.service.SysUserService;
import com.cellinfo.service.UtilService;
import com.cellinfo.utils.ResultUtil;
import com.cellinfo.utils.ReturnDesc;

/**
 * 系统管理接口
 * 组织管理
 * 组织管理员用户管理
 * 数据字典管理
 * @author admin
 *
 */
@ServiceLog(moduleName = "系统管理")
@PreAuthorize("hasRole('ROLE_ADMIN')")  
@RestController
@RequestMapping("/service/api")
public class SysAdminController {

	private final static Logger logger = LoggerFactory.getLogger(SysAdminController.class);
	
	@Autowired
	private UtilService utilService;
	
	@Autowired
	private SysUserService sysUserService;
	
	@Autowired
	private SysGroupService sysGroupService;
	
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

	/**
	 * 查询所有
	 * 
	 * @return
	 */
	@PostMapping(value = "/users")
	public Result<Page<TlGammaUser>> userList(@RequestBody RequestParameter para, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		logger.info("userList");
		int pageNumber = para.getPage();
		int pageSize = para.getPageSize();
		
		//todo

		Sort sort = null;
		if (para.getSortDirection().equalsIgnoreCase("ASC")) {
			sort = new Sort(Direction.ASC, para.getSortField());
		} else {
			sort = new Sort(Direction.DESC, para.getSortField());
		}

		PageRequest pageInfo = new PageRequest(pageNumber, pageSize, sort);
		Page<TlGammaUser> mList = this.sysUserService.getAll(pageInfo);
		return ResultUtil.success(mList);
	}

	/**
	 * 验证用户名是否存在
	 */
	public void testUsername ()
	{
		
	}
	
	/**
	 * 
	 * @param user
	 * @param bindingResult
	 * @return
	 */
	@Transactional
	@PostMapping(value = "/addUser")
	public Result<TlGammaUser> addUser(@RequestBody @Valid UserParameter user, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		TlGammaUser realUser = this.sysUserService.findOne(user.getUserName());
		if(realUser!=null && realUser.getUsername().length()>1)
		{
			return ResultUtil.error(400, ReturnDesc.USER_NAME_IS_EXIST);
		}
			
		realUser = new TlGammaUser();
		realUser.setUserCnname(user.getUserCnname());
		realUser.setGroupGuid(user.getGroupGuid());
		realUser.setUserName(user.getUserName());
		realUser.setUserGuid(UUID.randomUUID().toString());
		realUser.setRoleId("ROLE_GROUP_ADMIN");

		if(!user.getUserPassword().equalsIgnoreCase(realUser.getUserPassword()) )
			realUser.setUserPassword(encoder.encode(user.getUserPassword()));
		return ResultUtil.success(this.sysUserService.save(realUser));
	}
	/**
	 * 添加组织管理员用户，修改用户信息
	 * @param user
	 * @param bindingResult
	 * @return
	 */
	@Transactional
	@PostMapping(value = "/updateUser")
	public Result<TlGammaUser> updateUser(@RequestBody @Valid UserParameter user, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		TlGammaUser realUser = this.sysUserService.findOne(user.getUserName());
		if(realUser!=null && realUser.getUsername().length()>1)
		{
			realUser = new TlGammaUser();
			realUser.setUserCnname(user.getUserCnname());
			realUser.setRoleId("ROLE_GROUP_ADMIN");

			if(! user.getUserPassword().equalsIgnoreCase(realUser.getUserPassword()) )
				realUser.setUserPassword(encoder.encode(user.getUserPassword()));
			return ResultUtil.success(this.sysUserService.save(realUser));
		}			
		return ResultUtil.error(400, ReturnDesc.USER_NAME_IS_EXIST);
	}
	
//	
//	@PostMapping(value = "/deleteUser")
//	public Result deleteUser(@RequestBody @Valid TlGammaUser user, BindingResult bindingResult) {
//		if (bindingResult.hasErrors()) {
//			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
//		} 
//		this.sysUserService.delete(user);
//		return ResultUtil.success();
//	}
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	@GetMapping(value = "/users/{id}")
	public Result<TlGammaUser> userFindOne(@PathVariable("id") String userId) {
		return ResultUtil.success(sysUserService.findOne(userId));
	}

	// 更新
	@PostMapping(value = "/updateuser/{id}")
	public TlGammaUser userUpdate(@PathVariable("id") String id, @RequestParam("userCnname") String userCnname,
			@RequestParam("userPassword") String userPassword) {
		TlGammaUser user = new TlGammaUser();
		user.setUserName(id);
		user.setUserCnname(userCnname);
		user.setUserPassword(userPassword);
		return sysUserService.save(user);
	}

	// 删除
	@PostMapping(value = "/deleteuser/{id}")
	public void userDelete(@PathVariable("id") String id) {
		sysUserService.delete(id);
	}

	@GetMapping(value = "/users/cnname/{cnname}")
	public List<TlGammaUser> userListByAge(@PathVariable("cnname") String cnname) {
		return sysUserService.findByUserCnname(cnname);
	}

	@GetMapping(value = "users/getEnabled/{id}")
	public void getEnabled(@PathVariable("id") String userId) throws Exception {
		sysUserService.getEnabled(userId);
	}
	
	
	@PostMapping(value = "/groups")
	public Result<Page<TlGammaGroup>> groupList(@RequestBody RequestParameter para, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		logger.info("userList");
		int pageNumber = para.getPage();
		int pageSize = para.getPageSize();
		
		String sortField = "groupName";
		if(para.getSortField().equalsIgnoreCase("name"))
			sortField = "groupName";

		Sort sort = null;
		if (para.getSortDirection().equalsIgnoreCase("ASC")) {
			sort = new Sort(Direction.ASC, sortField);
		} else {
			sort = new Sort(Direction.DESC, sortField);
		}

		if(para.getSkey()!=null&&para.getSkey().length()>0)
		{
			// todo
		}
		if(para.getStatus()>0)
		{
			//todo
		}
		
		PageRequest pageInfo = new PageRequest(pageNumber, pageSize, sort);
		Page<TlGammaGroup> mList = this.sysGroupService.getGroupList(pageInfo);
		return ResultUtil.success(mList);
	}
	/**
	 * 创建组织
	 * @param group
	 * @param bindingResult
	 * @return
	 */
	@PostMapping(value = "/group/add")
	public Result<TlGammaGroup> addGroup(@RequestBody @Valid TlGammaGroup group, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		Iterable<TlGammaGroup> grouplist = this.sysGroupService.getByName(group.getGroupName());
		if(grouplist!=null && grouplist.iterator().hasNext())
		{
			return ResultUtil.error(400, ReturnDesc.GROUP_NAME_IS_EXIST);
		}
		group.setGroupGuid(UUID.randomUUID().toString());
		return ResultUtil.success(this.sysGroupService.addGroup(group));
	}
	/**
	 * 验证组织名是否存在
	 * @param groupname
	 * @return
	 */
	@PostMapping(value = "/group/testname")
	public Result<String> testGroupName(@RequestBody String groupname) {
		Map<String ,String > status = new HashMap<String,String>();
		
		///////
		return ResultUtil.success("ok");
	}
	
	@PostMapping(value = "/group/update")
	public Result<TlGammaGroup> updateGroup(@RequestBody @Valid TlGammaGroup group, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		TlGammaGroup refgroup = this.sysGroupService.getByGUID(group.getGroupGuid());
		if(refgroup!=null && refgroup.getGroupGuid().length()>1)
		{
			return ResultUtil.success(this.sysGroupService.updateGroup(group));
		}
		return ResultUtil.error(400, ReturnDesc.GROUP_NAME_IS_NOT_EXIST);
	}
	
//	@PostMapping(value = "/group/delete")
//	public Result<String> updateGroup(@RequestBody @Valid String groupGuid) {
//
//		return ResultUtil.success(this.sysGroupService.deleteGroup(groupGuid));
//	}
	
	///添加验证组织服务接口功能
	public boolean testService(String serviceUrl)
	{
		return true;
	}
		
}
