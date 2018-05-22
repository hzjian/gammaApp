package com.cellinfo.controller;

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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cellinfo.annotation.OperLog;
import com.cellinfo.annotation.ServiceLog;
import com.cellinfo.controller.entity.DictItemParameter;
import com.cellinfo.controller.entity.RequestParameter;
import com.cellinfo.controller.entity.UserParameter;
import com.cellinfo.entity.Result;
import com.cellinfo.entity.TlGammaDict;
import com.cellinfo.entity.TlGammaDictItem;
import com.cellinfo.entity.TlGammaGroup;
import com.cellinfo.entity.TlGammaUser;
import com.cellinfo.security.UserInfo;
import com.cellinfo.service.DictParameter;
import com.cellinfo.service.SysDictService;
import com.cellinfo.service.SysGroupService;
import com.cellinfo.service.SysUserService;
import com.cellinfo.service.UtilService;
import com.cellinfo.utils.FuncDesc;
import com.cellinfo.utils.OperDesc;
import com.cellinfo.utils.ResultUtil;
import com.cellinfo.utils.ReturnDesc;

@ServiceLog(moduleName = "系统通用模块")
@RestController
@RequestMapping("/service/common")
public class SysCommonController {
private final static Logger logger = LoggerFactory.getLogger(SysCommonController.class);
	
	@Autowired
	private UtilService utilService;
	
	@Autowired
	private SysUserService sysUserService;
	
	@Autowired
	private SysGroupService sysGroupService;
	
	@Autowired
	private SysDictService sysDictService;
	private BCryptPasswordEncoder  encoder =new BCryptPasswordEncoder();
	private final static String DEFAULT_PASSWORD = "PASSWORD";
	
	@OperLog(funcName = FuncDesc.QUERY_CURRENT_USER_INFO, methodName = OperDesc.QUERY)
	@PostMapping(value = "/userinfo")
	public Result<Map<String, String>> getCurrentUserInfo(HttpServletRequest request) {
		Map<String, String> tMap = new HashMap<String, String>();
		//String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
		
		UserInfo cUser = this.utilService.getCurrentUser(request);
		if (cUser!=null && cUser.getUserName().length()>0) {

			Optional<TlGammaUser> userOptional = this.sysUserService.findOne(cUser.getUserName());
			if(userOptional.isPresent())
			{
				TlGammaUser user = userOptional.get();
				logger.info("user=="+user);
				tMap.put("username", user.getUsername());
				tMap.put("usercnname", user.getUserCnname());	
				String roleName = "组织用户";
				Integer roleKey = 300;
				if(user.getRoleId().equalsIgnoreCase("ROLE_ADMIN"))
				{
					roleName= "系统管理员";
					roleKey = 100;
				}
				else if(user.getRoleId().equalsIgnoreCase("ROLE_GROUP_ADMIN"))
				{
					roleName = "组织管理员";
					roleKey = 200;
				}
				tMap.put("rolename", roleName);
				if(cUser.getGroupGuid()!= null)
				{
					Optional<TlGammaGroup> group = this.sysGroupService.findOne(cUser.getGroupGuid());
					if(group.isPresent())
					{
						tMap.put("groupname", group.get().getGroupName());
						tMap.put("roleKey", String.valueOf(roleKey));
					}
				}
			}
			return ResultUtil.success(tMap);
        } 
		return ResultUtil.error(400, ReturnDesc.USER_INFO_IS_ILLEGAL);
	}

	@OperLog(funcName = FuncDesc.MODIFY_CURRENT_USER_INFO, methodName = OperDesc.EDIT)
	@PostMapping(value = "/updateuser")
	public Result<UserParameter> updateUser(HttpServletRequest request,@RequestBody @Valid UserParameter userInfo, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		UserInfo cUser = this.utilService.getCurrentUser(request);
		if(userInfo.getUserName()!= null && cUser!=null)
		{
			Optional<TlGammaUser> realUserOptional = this.sysUserService.findOne(cUser.getUserName());
			if(realUserOptional.isPresent() && realUserOptional.get().getUsername().length()>1)
			{
				TlGammaUser realUser = realUserOptional.get();
				if(userInfo.getUserCnname()!=null)
					realUser.setUserCnname(userInfo.getUserCnname());
				if(userInfo.getUserEmail()!=null)
					realUser.setUserEmail(userInfo.getUserEmail());
	
				if(userInfo.getUserPassword()!=null && !userInfo.getUserPassword().equalsIgnoreCase(DEFAULT_PASSWORD))
					realUser.setUserPassword(encoder.encode(userInfo.getUserPassword()));	
				
				TlGammaUser saveUser = this.sysUserService.save(realUser);
				UserParameter resUser= new UserParameter();
				resUser.setUserName(saveUser.getUsername());
				resUser.setUserEmail(saveUser.getUserEmail());
				resUser.setUserCnname(saveUser.getUserCnname());
				return ResultUtil.success(resUser);
			}
		}
		return ResultUtil.error(400, ReturnDesc.USER_NAME_IS_NOT_EXIST);
	}
	
	@PostMapping(value = "/dicts")
	public Result<List<Map<String, Object>>> groupDict(HttpServletRequest request ,@RequestBody RequestParameter para, BindingResult bindingResult) {
		List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
		UserInfo cUser = this.utilService.getCurrentUser(request);
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		int pageNumber = para.getPage();
		int pageSize = para.getPageSize();

		Sort sort = null;
		String sortField ="dictName";
		
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
		PageRequest pageInfo =  PageRequest.of(pageNumber, pageSize, sort);
		Page<TlGammaDict> mList = this.sysDictService.getDictsByGroupGuid(cUser.getGroupGuid(),filterStr,pageInfo);
		for (TlGammaDict eachDict : mList) { 
			Map<String, Object> tMap = new HashMap<String, Object>();
			tMap.put("dictId", eachDict.getDictId());
			tMap.put("dictName", eachDict.getDictName());
			tMap.put("dictDesc", eachDict.getDictDesc());
			list.add(tMap);
		}
		
		return ResultUtil.success(list);
	}
	
	@PostMapping(value = "/dict/save")
	public Result<TlGammaDict> addDict(HttpServletRequest request ,@RequestBody @Valid DictParameter dict, BindingResult bindingResult) {
		UserInfo cUser = this.utilService.getCurrentUser(request);
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}

		List<TlGammaDict> dictList = this.sysDictService.getDictbyName(dict.getDictName(),cUser.getGroupGuid());
		if(dictList!=null && dictList.iterator().hasNext())
		{
			return ResultUtil.error(400, ReturnDesc.DICT_NAME_IS_EXIST);
		}
		
		TlGammaDict tmpDict = new TlGammaDict();
		if(dict.getDictId()== null || dict.getDictId().length()<5)
			tmpDict.setDictId(UUID.randomUUID().toString());
		else
			tmpDict.setDictId(dict.getDictId());
			
		tmpDict.setGroupGuid(cUser.getGroupGuid());
		tmpDict.setDictName(dict.getDictName());
		tmpDict.setDictDesc(dict.getDictDesc());
		this.sysDictService.save(tmpDict);
		return ResultUtil.success(tmpDict);
	}
	

	@PostMapping(value = "/dict/items")
	public Result<List<TlGammaDictItem>> dictItems(HttpServletRequest request ,@RequestBody @Valid DictParameter dict, BindingResult bindingResult) {
		UserInfo cUser = this.utilService.getCurrentUser(request);
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}

		if(dict.getDictId()!= null && dict.getDictId().length()>1) {
			List<TlGammaDictItem> itemList = this.sysDictService.getDictItems(dict.getDictId());
			return ResultUtil.success(itemList);
		}
		return ResultUtil.error(400, ReturnDesc.INPUT_PARAMETER_ERROR);
	}
	//删除数据字典
	
	@PostMapping(value = "/dict/delete")
	public Result<String> deleteDict(HttpServletRequest request ,@RequestBody @Valid DictParameter dict, BindingResult bindingResult) {
		UserInfo cUser = this.utilService.getCurrentUser(request);
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}

		if(dict.getDictId()!= null && dict.getDictId().length()>1) {
			this.sysDictService.deleteDict(dict.getDictId());
		}
		return ResultUtil.success(ReturnDesc.EXECUTION_SUCCESS);
	}
	
	//添加数据字典项目
	@PostMapping(value = "/dict/additem")
	public Result<TlGammaDictItem> addDictItem(HttpServletRequest request ,@RequestBody @Valid DictItemParameter item, BindingResult bindingResult) {
		UserInfo cUser = this.utilService.getCurrentUser(request);
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}

		List<TlGammaDictItem> dictItemList = this.sysDictService.getDictItemByName(item.getDictId(),item.getDictItem());
		if(dictItemList!=null && dictItemList.iterator().hasNext())
		{
			return ResultUtil.error(400, ReturnDesc.DICT_ITEM_NAME_IS_EXIST);
		}
		
		if(item.getDictId()!= null && item.getDictId().length()>1 && 
				item.getDictItem()!= null && item.getDictItem().length()>1) {
			return ResultUtil.success(this.sysDictService.addDictItem(item.getDictId(),item.getDictItem()));
		}
		return ResultUtil.error(400, ReturnDesc.INPUT_PARAMETER_ERROR);
	}
	
	//删除数据字典项目
	@PostMapping(value = "/dict/deleteitem")
	public Result<String> deleteDictItem(HttpServletRequest request ,@RequestBody @Valid Long itemId, BindingResult bindingResult) {
		UserInfo cUser = this.utilService.getCurrentUser(request);
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}

		this.sysDictService.deleteItem(itemId);
		return ResultUtil.success(ReturnDesc.EXECUTION_SUCCESS);
	}
}
