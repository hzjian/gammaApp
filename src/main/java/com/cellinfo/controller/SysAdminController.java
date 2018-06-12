package com.cellinfo.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cellinfo.annotation.OperLog;
import com.cellinfo.annotation.ServiceLog;
import com.cellinfo.controller.entity.GAParameter;
import com.cellinfo.controller.entity.GroupParameter;
import com.cellinfo.controller.entity.RequestParameter;
import com.cellinfo.controller.entity.UserParameter;
import com.cellinfo.entity.Result;
import com.cellinfo.entity.TlGammaGroup;
import com.cellinfo.entity.TlGammaUser;
import com.cellinfo.service.SysGroupService;
import com.cellinfo.service.SysUserService;
import com.cellinfo.service.UtilService;
import com.cellinfo.utils.FuncDesc;
import com.cellinfo.utils.OperDesc;
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
	
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	private final static String DEFAULT_PASSWORD = "PASSWORD";

	/**
	 * 查询所有
	 * 
	 * @return
	 */
	@OperLog(funcName = FuncDesc.GROUP_ADMIN_USER_LIST, methodName = OperDesc.QUERY)
	@PostMapping(value = "/users")
	public Result<Page<UserParameter>> userList(@RequestBody RequestParameter para, BindingResult bindingResult) {
		
		List<Map<String,Object>> mlist = new LinkedList<Map<String,Object>>();
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		logger.info("userList");
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
		Page<TlGammaUser> tmpList = null;
		
		if(para.getGroupId()!= null)
		{
			tmpList = this.sysUserService.getGroupAdminUsers(filterStr,para.getGroupId(),pageInfo);
		}
		else
		{
			tmpList = this.sysUserService.getGroupAdminUsers(filterStr,pageInfo);
		}
		
		if(tmpList!= null)
		{
			mlist = tmpList.getContent().stream().map(item -> {
				Map<String,Object> tmp = new HashMap<String,Object>();
				tmp.put("groupId", item.getGroupGuid());
				tmp.put("userCnname",item.getUserCnname());
				if(item.getGroupGuid()!=null)
				{
					Optional<TlGammaGroup> group = this.sysGroupService.findOne(item.getGroupGuid());
					if(group.isPresent())
						tmp.put("groupName",group.get().getGroupName());
				}
				tmp.put("userName",item.getUsername());
				tmp.put("userStatus", item.getAccountStatus());
				tmp.put("userEmail", item.getUserEmail());
				if(item.getLoginTime()!= null)
					tmp.put("lastLoginTime", df.format(item.getLoginTime()));
				return tmp;
			}).collect(Collectors.toList());
			Page<Map<String,Object>> userPage = new PageImpl<Map<String,Object>>(mlist,pageInfo,tmpList.getTotalElements());
			return ResultUtil.success(userPage);
		}
		return ResultUtil.error(400, ReturnDesc.UNKNOW_ERROR);
	}

	@PostMapping(value = "/user")
	public Result<Map<String,Object>> adminUser(@RequestBody GAParameter para, BindingResult bindingResult) {
		Map<String,Object> tmp = new HashMap<String,Object>();
		if(para.getId()!= null)
		{
			TlGammaUser tmpUser = this.sysUserService.getGroupAdminUser(para.getId());
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

	/**
	 * 验证用户名是否存在
	 */
	@PostMapping(value = "/user/testname")
	public Result<TlGammaUser> testGroupMemberName(HttpServletRequest request,@RequestBody @Valid String membername, BindingResult bindingResult) {	
		Optional<TlGammaUser> kuser = this.sysUserService.findOne(membername);
		if(kuser.isPresent() )
		{
			return ResultUtil.error(400, ReturnDesc.KERNEL_NAME_IS_EXIST);
		}
		return ResultUtil.success(membername);
	}
	
	/**
	 * 添加用户
	 * @param user
	 * @param bindingResult
	 * @return
	 */
	//@Transactional
	@PostMapping(value = "/user/save")
	public Result<UserParameter> addUser(@RequestBody @Valid UserParameter user, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		Optional<TlGammaUser> kuser = this.sysUserService.findOne(user.getUserName());
		if(kuser.isPresent())
			return ResultUtil.error(400, ReturnDesc.USER_NAME_IS_EXIST);
		
		TlGammaUser tmpUser = new TlGammaUser();
		tmpUser.setUserGuid(UUID.randomUUID().toString());
		tmpUser.setGroupGuid(user.getGroupGuid());
		tmpUser.setRoleId("ROLE_GROUP_ADMIN");
		tmpUser.setUserEmail(user.getUserEmail());
		tmpUser.setUserCnname(user.getUserCnname());
		tmpUser.setUserName(user.getUserName());
		tmpUser.setUserPassword(encoder.encode(user.getUserPassword()));
		tmpUser.setAccountEnabled(true);
		tmpUser.setAccountNonExpired(true);
		tmpUser.setAccountNonLocked(true);
		tmpUser.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		UserParameter resUser= new UserParameter();
		TlGammaUser saveUser = this.sysUserService.save(tmpUser);
		Map<String,String> saveResult = new HashMap<String,String>();
		//resUser.setUserGuid(saveUser.getUserGuid());
		saveResult.put("userName", saveUser.getUsername());
		saveResult.put("userCnname", saveUser.getUserCnname());
		return ResultUtil.success(saveResult);
	}
	/**
	 * 添加组织管理员用户，修改用户信息
	 * @param user
	 * @param bindingResult
	 * @return
	 */
	//@Transactional
	@PostMapping(value = "/user/update")
	public Result<UserParameter> updateUser(@RequestBody @Valid UserParameter user, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		if(user.getUserName()!= null)
		{
			Optional<TlGammaUser> realUserOptional = this.sysUserService.findOne(user.getUserName());
			if(realUserOptional.isPresent() && realUserOptional.get().getUsername().length()>1)
			{
				TlGammaUser  realUser = realUserOptional.get();
				if(user.getUserCnname()!=null)
					realUser.setUserCnname(user.getUserCnname());
				if(user.getUserEmail()!=null)
					realUser.setUserEmail(user.getUserEmail());
				realUser.setRoleId("ROLE_GROUP_ADMIN");
	
				if(user.getUserPassword()!=null && !user.getUserPassword().equalsIgnoreCase(DEFAULT_PASSWORD))
					realUser.setUserPassword(encoder.encode(user.getUserPassword()));	
				if(user.getUserStatus()!=null)
					realUser.setAccountEnabled(user.getUserStatus()==1?true:false);
				realUser.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				UserParameter resUser= new UserParameter();
				TlGammaUser saveUser = this.sysUserService.save(realUser);
				//resUser.setUserGuid(saveUser.getUserGuid());
				resUser.setUserName(saveUser.getUsername());
				resUser.setUserEmail(saveUser.getUserEmail());
				resUser.setUserCnname(saveUser.getUserCnname());
				return ResultUtil.success(resUser);
			}	
		}
		return ResultUtil.error(400, ReturnDesc.USER_NAME_IS_NOT_EXIST);
	}
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	@GetMapping(value = "/user/query")
	public Result<UserParameter> userFindOne( @RequestBody @Valid String userName) {
		
		Optional<TlGammaUser>  realuser = sysUserService.findOne(userName);
		if(!realuser.isPresent())
			return ResultUtil.error(400, ReturnDesc.USER_NAME_IS_NOT_EXIST);
		
		UserParameter user = new UserParameter();
		user.setGroupGuid(realuser.get().getGroupGuid());
		user.setUserCnname(realuser.get().getUserCnname());
		user.setUserName(user.getUserName());

		return ResultUtil.success(user);
	}
	
	
	@PostMapping(value = "/groups")
	public Result<Page<TlGammaGroup>> groupList(@RequestBody RequestParameter para, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		logger.info("userList");
		int pageNumber = para.getPage();
		int pageSize = para.getPageSize();
		
		String sortField = "updateTime";
		if(para.getSortField()!= null && para.getSortField().equalsIgnoreCase("name"))
			sortField = "groupName";

		Sort sort = null;
		if (para.getSortDirection().equalsIgnoreCase("ASC")) {
			sort = new Sort(Direction.ASC, sortField);
		} else {
			sort = new Sort(Direction.DESC, sortField);
		}
		PageRequest pageInfo = new PageRequest(pageNumber, pageSize, sort);
		String filterStr ="";
		if(para.getSkey()!=null&&para.getSkey().length()>0)
		{
			filterStr = para.getSkey();
		}
		
		Page<TlGammaGroup> mList = this.sysGroupService.queryGroupListByName(filterStr,pageInfo);
		return ResultUtil.success(mList);
	}
	
	@PostMapping(value = "/group")
	public Result<Map<String,Object>> groupInfo(@RequestBody GAParameter para) {
		Map<String,Object> tmp = new HashMap<String,Object>();
		if(para.getId()!=null)
		{
			Optional<TlGammaGroup>  groupOptional =  this.sysGroupService.findOne(para.getId());
			if(groupOptional.isPresent())
			{
				TlGammaGroup tGroup = groupOptional.get();
				tmp.put("groupAddress", tGroup.getGroupAddress());
				tmp.put("groupCode", tGroup.getGroupCode());
				tmp.put("groupEmail", tGroup.getGroupEmail());
				tmp.put("groupId", tGroup.getGroupGuid());
				tmp.put("groupName", tGroup.getGroupName());
				tmp.put("groupPhone", tGroup.getGroupPhone());
				tmp.put("groupStatus", tGroup.getGroupStatus());
			}
		}
		
		return ResultUtil.success(tmp);
	}
	/**
	 * 创建组织
	 * @param group
	 * @param bindingResult
	 * @return
	 */
	@PostMapping(value = "/group/save")
	public Result<TlGammaGroup> addGroup(@RequestBody @Valid GroupParameter group, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		Iterable<TlGammaGroup> grouplist = this.sysGroupService.getByName(group.getGroupName());
		if(grouplist!=null && grouplist.iterator().hasNext())
		{
			return ResultUtil.error(400, ReturnDesc.GROUP_NAME_IS_EXIST);
		}
		TlGammaGroup refgroup = new TlGammaGroup();
		refgroup.setGroupGuid(UUID.randomUUID().toString());
		refgroup.setGroupAddress(group.getGroupAddress());
		refgroup.setGroupCode(group.getGroupCode());
		refgroup.setGroupEmail(group.getGroupEmail());
		refgroup.setGroupName(group.getGroupName());
		refgroup.setGroupPhone(group.getGroupPhone());
		refgroup.setGroupPic(group.getGroupPic());
		refgroup.setGroupService(group.getGroupService());
		refgroup.setGroupStatus(group.getGroupStatus());
		refgroup.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		
		return ResultUtil.success(this.sysGroupService.addGroup(refgroup));
	}
	/**
	 * 验证组织名是否存在
	 * @param groupname
	 * @return
	 */
	@PostMapping(value = "/group/testname")
	public Result<String> testGroupName(@RequestBody String groupname) {
		Map<String ,String > status = new HashMap<String,String>();
		List<TlGammaGroup> gList = this.sysGroupService.findByGroupName(groupname);
		if(gList!=null && gList.size()>0 )
		{
			return ResultUtil.error(400, ReturnDesc.KERNEL_NAME_IS_EXIST);
		}
		return ResultUtil.success(groupname);
	}
	
	@PostMapping(value = "/group/update")
	public Result<TlGammaGroup> updateGroup(@RequestBody @Valid GroupParameter group, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		Optional<TlGammaGroup> refgroupOptional = this.sysGroupService.getByGUID(group.getGroupGuid());
		if(refgroupOptional.isPresent() && refgroupOptional.get().getGroupGuid().length()>1)
		{
			TlGammaGroup refgroup = refgroupOptional.get();
			if(group.getGroupAddress()!=null)
				refgroup.setGroupAddress(group.getGroupAddress());
			if(group.getGroupCode()!=null)
				refgroup.setGroupCode(group.getGroupCode());
			if(group.getGroupEmail()!=null)
				refgroup.setGroupEmail(group.getGroupEmail());
			if(group.getGroupName()!=null)
				refgroup.setGroupName(group.getGroupName());
			if(group.getGroupPhone()!=null)
				refgroup.setGroupPhone(group.getGroupPhone());
			if(group.getGroupPic()!=null)
				refgroup.setGroupPic(group.getGroupPic());
			if(group.getGroupService()!=null)
				refgroup.setGroupService(group.getGroupService());
			if(group.getGroupStatus()!=null)
				refgroup.setGroupStatus(group.getGroupStatus());
			refgroup.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			return ResultUtil.success(this.sysGroupService.updateGroup(refgroup));
		}
		return ResultUtil.error(400, ReturnDesc.GROUP_NAME_IS_NOT_EXIST);
	}
	
	///添加验证组织服务接口功能
	public boolean testService(String serviceUrl)
	{
		return true;
	}
		
}
