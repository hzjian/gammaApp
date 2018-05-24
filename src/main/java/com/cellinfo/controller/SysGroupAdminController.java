package com.cellinfo.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cellinfo.annotation.ServiceLog;
import com.cellinfo.controller.entity.AttrParameter;
import com.cellinfo.controller.entity.KernelParameter;
import com.cellinfo.controller.entity.RequestParameter;
import com.cellinfo.controller.entity.UserParameter;
import com.cellinfo.entity.Result;
import com.cellinfo.entity.TlGammaKernel;
import com.cellinfo.entity.TlGammaKernelAttr;
import com.cellinfo.entity.TlGammaTask;
import com.cellinfo.entity.TlGammaUser;
import com.cellinfo.entity.ViewTaskUser;
import com.cellinfo.security.UserInfo;
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
	
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");   
	
	private BCryptPasswordEncoder  encoder =new BCryptPasswordEncoder();
	
	private final static String DEFAULT_PASSWORD = "PASSWORD";

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
	
	@PostMapping(value = "/kernel/query")
	public Result<Map<String,Object>> queryKernel(HttpServletRequest request ,@RequestBody @Valid String kernelClassid) {
		UserInfo cUser = this.utilService.getCurrentUser(request);
		Map<String,Object> result = new HashMap<String,Object>();
		Optional<TlGammaKernel> kernel = this.sysKernelService.getByKernelClassid(kernelClassid);
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
			tmpKernel.setKernelClassname(kernel.getClassName());
		if(kernel.getGeomType()!=null)
			tmpKernel.setGeomType(kernel.getGeomType());

		Map<String,String> result = new HashMap<String,String>();
		TlGammaKernel saveKernel = this.sysKernelService.addGroupKernel(tmpKernel);
		
		result.put("classId", saveKernel.getKernelClassid());
		result.put("className", saveKernel.getKernelClassname());
		
		return ResultUtil.success(result);
	}
	
	@PostMapping(value = "/kernel/addattr")
	public Result<Map<String,String>> addKernelAttr(HttpServletRequest request ,@RequestBody @Valid AttrParameter attrPara, BindingResult bindingResult) {
		UserInfo cUser = this.utilService.getCurrentUser(request);
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		Map<String,String> result = new HashMap<String,String>();
		TlGammaKernelAttr  attr = new TlGammaKernelAttr();
		attr.setAttrGuid(UUID.randomUUID().toString());
		attr.setKernelClassid(attrPara.getClassId());
		attr.setAttrName(attrPara.getAttrName());
		attr.setAttrField("F_"+this.utilService.generateShortUuid());
		attr.setAttrType(attrPara.getAttrType());
		attr.setAttrEnum(attrPara.getAttrEnum());
		attr.setAttrFgrade(attrPara.getAttrGrade());
		TlGammaKernelAttr  tmp = this.sysKernelService.saveKernelAttr(attr);

		result.put("attrId", tmp.getAttrGuid());
		result.put("attrName", tmp.getAttrName());

		return ResultUtil.success(result);
	}
	
	@PostMapping(value = "/kernel/deleteattr")
	public Result<String> deleteKernelAttr(HttpServletRequest request ,@RequestBody @Valid AttrParameter attrPara, BindingResult bindingResult) {
		UserInfo cUser = this.utilService.getCurrentUser(request);
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		Map<String,String> result = new HashMap<String,String>();
		long aNum = this.sysTaskService.getAttrApplyNum(attrPara.getAttrId());
		if(aNum>0)///判断该字段是否被使用
		{
			this.sysKernelService.deleteKernelAttr(attrPara.getAttrId());
			return ResultUtil.success(ReturnDesc.EXECUTION_SUCCESS);
		}
		return ResultUtil.error(400,ReturnDesc.THIS_ATTR_IS_INUSED);
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
		String sortField ="userName";
		
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
		
		PageRequest pageInfo = PageRequest.of(pageNumber, pageSize, sort);
		Page<TlGammaUser> mList = this.sysUserService.getGroupMember(pageInfo,filterStr,cUser.getGroupGuid());
		for (TlGammaUser eachUser : mList) {
			Map<String, Object> tMap = new HashMap<String, Object>();
			tMap.put("userName", eachUser.getUserName());
			tMap.put("userCnname", eachUser.getUserCnname());
			tMap.put("userEmail",eachUser.getUserEmail());
			list.add(tMap);
		}
		Page<Map<String, Object>> resPage = new PageImpl<Map<String, Object>>(list,pageInfo,mList.getTotalElements());
		return ResultUtil.success(resPage);
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
		
		PageRequest pageInfo = PageRequest.of(pageNumber, pageSize, sort);
		Page<TlGammaTask> mList = this.sysTaskService.getTaskByGroupGuid(cUser.getGroupGuid(),pageInfo);
		for (TlGammaTask eachTask : mList) {
			Map<String, Object> tMap = new HashMap<String, Object>(); 
			tMap.put("taskGuid", eachTask.getTaskGuid());
			tMap.put("taskName", eachTask.getTaskName());
			if(eachTask.getTaskTimestart()!= null)
				tMap.put("startDate", df.format(eachTask.getTaskTimestart()));
			if(eachTask.getTaskTimeend()!= null)
				tMap.put("endDate", df.format(eachTask.getTaskTimeend()));
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
		
		PageRequest pageInfo = PageRequest.of(pageNumber, pageSize, sort);
		Page<TlGammaTask> mList = this.sysTaskService.getTaskByUsername(para.getSkey(),pageInfo);
		for (TlGammaTask eachTask : mList) {
			Map<String, Object> tMap = new HashMap<String, Object>(); 
			tMap.put("taskGuid", eachTask.getTaskGuid());
			tMap.put("taskName", eachTask.getTaskName());
			if(eachTask.getTaskTimestart()!= null)
				tMap.put("startDate", df.format(eachTask.getTaskTimestart()));
			if(eachTask.getTaskTimeend()!= null)
				tMap.put("endDate", df.format(eachTask.getTaskTimeend()));
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
		
		PageRequest pageInfo = PageRequest.of(pageNumber, pageSize, sort);
		Page<ViewTaskUser> mList = this.sysTaskService.getTaskByUserParticapate(para.getSkey(),pageInfo);
		for (ViewTaskUser eachTask : mList) {
			Map<String, Object> tMap = new HashMap<String, Object>(); 
			tMap.put("taskGuid", eachTask.getId().getTaskGuid());
			tMap.put("taskName", eachTask.getTaskName());
			if(eachTask.getTaskTimestart()!= null)
				tMap.put("startDate", df.format(eachTask.getTaskTimestart()));
			if(eachTask.getTaskTimeend()!= null)
				tMap.put("endDate", df.format(eachTask.getTaskTimeend()));
			tMap.put("userName", eachTask.getId().getUserName());
			list.add(tMap);
		}
		Page<Map<String, Object>> resPage = new PageImpl<Map<String, Object>>(list,pageInfo,mList.getTotalElements());
		return ResultUtil.success(resPage);
	}
}
