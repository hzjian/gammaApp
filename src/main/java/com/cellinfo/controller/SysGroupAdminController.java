package com.cellinfo.controller;

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
import com.cellinfo.controller.entity.FieldParameter;
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
	
	@PostMapping(value = "/curUserInfo")
	public Result<Map<String, String>> getCurrentUserInfo(HttpServletRequest request) {
		Map<String, String> tMap = new HashMap<String, String>();
		//String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
		
		UserInfo currentUser = this.utilService.getCurrentUser(request);
		if (currentUser!=null && currentUser.getUserName().length()>0) {

			logger.info("currentUser=="+currentUser);
			Optional<TlGammaUser> user = this.sysUserService.findOne(currentUser.getUserName());
			logger.info("user=="+user);
			tMap.put("username", user.get().getUsername());
			tMap.put("usercnname", user.get().getUserCnname());
			
			return ResultUtil.success(tMap);
        } 
		return ResultUtil.error(400, ReturnDesc.USER_INFO_IS_ILLEGAL);
	}

	@PostMapping(value = "/kernels")
	public Result<List<Map<String, Object>>> kernelList(HttpServletRequest request ,@RequestBody RequestParameter para, BindingResult bindingResult) {
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
		
		return ResultUtil.success(list);
	}
	
	@PostMapping(value = "/kernel/save")
	public Result<TlGammaKernel> addKernel(HttpServletRequest request ,@RequestBody @Valid KernelParameter kernel, BindingResult bindingResult) {
		UserInfo cUser = this.utilService.getCurrentUser(request);
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		TlGammaKernel tmpKernel = new TlGammaKernel();
		if(kernel.getClassGuid() == null || kernel.getClassGuid().trim().equalsIgnoreCase("0"))
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
		
		List<TlGammaKernelAttr> entities = new LinkedList<TlGammaKernelAttr>();
		for(FieldParameter field : kernel.getFieldList())
		{
			TlGammaKernelAttr  attr = new TlGammaKernelAttr();
			attr.setAttrGuid(UUID.randomUUID().toString());
			attr.setKernelClassid(tmpKernel.getKernelClassid());
			attr.setAttrName(field.getFieldName());
			attr.setAttrField("F_"+this.utilService.generateShortUuid());
//			   STRING
//			 * INTEGER
//			 * NUMBER
//			 * DATETIME
			attr.setAttrType(field.getFieldType());
			attr.setAttrEnum(field.getFieldEnum());
			attr.setAttrFgrade(field.getFieldGrade());
			entities.add(attr);
		}
		KernelParameter resKernel = new KernelParameter();
		this.sysKernelService.saveKernelAttr(entities);
		TlGammaKernel saveKernel = this.sysKernelService.addGroupKernel(tmpKernel);
		resKernel.setClassGuid(saveKernel.getKernelClassid());
		resKernel.setClassName(saveKernel.getKernelClassname());
		resKernel.setGeomType(saveKernel.getGeomType());
		resKernel.setDescInfo(saveKernel.getKernelClassdesc());
		return ResultUtil.success(resKernel);
	}
	
	@PostMapping(value = "/kernel/query")
	public Result<KernelParameter> queryKernel(HttpServletRequest request ,@RequestBody @Valid String kernelClassid) {
		UserInfo cUser = this.utilService.getCurrentUser(request);
		KernelParameter kPara = new KernelParameter();
		Optional<TlGammaKernel> kernel = this.sysKernelService.getByKernelClassid(kernelClassid);
		if(!kernel.isPresent() )
		{
			return ResultUtil.error(400, ReturnDesc.KERNEL_IS_NOT_EXIST);
		}
		
		kPara.setClassGuid(kernel.get().getKernelClassid());
		kPara.setClassName(kernel.get().getKernelClassname());
		kPara.setDescInfo(kernel.get().getKernelClassdesc());;
		kPara.setGeomType(kernel.get().getGeomType());
		
		List<TlGammaKernelAttr> attrList =  this.sysKernelService.getKernelAttrList(kernel.get().getKernelClassid());
		List<FieldParameter> fieldList = new LinkedList<FieldParameter>();
		for(TlGammaKernelAttr  attr : attrList)
		{		
			FieldParameter field = new FieldParameter();
			field.setFieldAlias(attr.getAttrField());
			field.setFieldEnum(attr.getAttrEnum());
			field.setFieldGrade(attr.getAttrFgrade());
			field.setFieldGuid(attr.getAttrGuid());
			field.setFieldName(attr.getAttrName());
			field.setFieldType(attr.getAttrType());
			
			fieldList.add(field);
		}
		kPara.setFieldList(fieldList);
		return ResultUtil.success(kPara);
	}
	
	@PostMapping(value = "/kernel/update")
	public Result<TlGammaKernel> updateKernel(HttpServletRequest request ,@RequestBody @Valid KernelParameter kernel, BindingResult bindingResult) {
		UserInfo cUser = this.utilService.getCurrentUser(request);
		if (bindingResult.hasErrors()) {
			return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
		}
		Optional<TlGammaKernel> tmpKernelOptional = this.sysKernelService.getByKernelClassid(kernel.getClassGuid());
		if(!tmpKernelOptional.isPresent() )
		{
			return ResultUtil.error(400, ReturnDesc.KERNEL_IS_NOT_EXIST);
		}
		TlGammaKernel tmpKernel =tmpKernelOptional.get();
		tmpKernel.setKernelClassdesc(kernel.getDescInfo());
		tmpKernel.setKernelClassname(kernel.getClassName());
		tmpKernel.setGeomType(kernel.getGeomType());
		
		
		List<TlGammaKernelAttr> entities = new LinkedList<TlGammaKernelAttr>();
		for(FieldParameter field : kernel.getAppendList())
		{
			TlGammaKernelAttr  attr = new TlGammaKernelAttr();
			attr.setAttrGuid(UUID.randomUUID().toString());
			attr.setKernelClassid(tmpKernel.getKernelClassid());
			attr.setAttrName(field.getFieldName());
			attr.setAttrField("F_"+this.utilService.generateShortUuid());
			attr.setAttrType(field.getFieldType());
			attr.setAttrEnum(field.getFieldEnum());
			attr.setAttrFgrade(field.getFieldGrade());
			entities.add(attr);
		}
		KernelParameter resKernel = new KernelParameter();
		this.sysKernelService.saveKernelAttr(entities);
		TlGammaKernel saveKernel = this.sysKernelService.addGroupKernel(tmpKernel);
		resKernel.setClassGuid(saveKernel.getKernelClassid());
		resKernel.setClassName(saveKernel.getKernelClassname());
		resKernel.setGeomType(saveKernel.getGeomType());
		resKernel.setDescInfo(saveKernel.getKernelClassdesc());
		return ResultUtil.success(resKernel);
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
		
		if (para.getSortDirection()!=null && para.getSortDirection().equalsIgnoreCase("ASC")) {
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
			tMap.put("userEmail",eachUser.getUserEmail());
			tMap.put("password", DEFAULT_PASSWORD);
			list.add(tMap);
		}
		
		return ResultUtil.success(list);
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
	public Result<TlGammaUser> saveGroupMember(HttpServletRequest request,@RequestBody @Valid UserParameter member, BindingResult bindingResult) {
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
		tmpUser.setUserEmail(member.getUserName());
		tmpUser.setRoleId("ROLE_USER");
		tmpUser.setUserCnname(member.getUserCnname());
		tmpUser.setUserName(member.getUserName());
		tmpUser.setUserPassword(encoder.encode(member.getUserPassword()));
		tmpUser.setAccountEnabled(true);
		tmpUser.setAccountNonExpired(true);
		tmpUser.setAccountNonLocked(true);
		
		return ResultUtil.success(this.sysUserService.save(tmpUser));
	}
	
	@PostMapping(value = "/member/update")
	public Result<TlGammaUser> updateGroupMember(@RequestBody @Valid UserParameter member, BindingResult bindingResult) {
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

		return ResultUtil.success(this.sysUserService.save(kuser));
	}
	
	@PostMapping(value = "/member/delete")
	public Result<String> deleteGroupMember(@RequestBody @Valid String username) {
		this.sysUserService.delete(username);
		return ResultUtil.success(ReturnDesc.EXECUTION_SUCCESS);
	}
	
	//组织任务查看
	@PostMapping(value = "/tasklist")
	public Result<List<Map<String, Object>>>  groupTaskList (HttpServletRequest request,@RequestBody @Valid RequestParameter para, BindingResult bindingResult) 
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
			tMap.put("taskGuid", eachTask.getTaskGuid());
			tMap.put("taskName", eachTask.getTaskName());
			if(eachTask.getTaskTimestart()!= null)
				tMap.put("startDate", df.format(eachTask.getTaskTimestart()));
			if(eachTask.getTaskTimeend()!= null)
				tMap.put("endDate", df.format(eachTask.getTaskTimeend()));
			tMap.put("userName", eachTask.getUserName());
			list.add(tMap);
		}

		return ResultUtil.success(list);
	}
	
	//组织用户创建任务查看
	@PostMapping(value = "/usertasklist")
	public Result<List<Map<String, Object>>>  userTaskList(HttpServletRequest request,@RequestBody @Valid RequestParameter para, BindingResult bindingResult) 
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

		return ResultUtil.success(list);
	}
	
	//组织用户参与任务查看
	// TODO
	@PostMapping(value = "/parttasklist")
	public Result<List<Map<String, Object>>>  partTaskList(HttpServletRequest request,@RequestBody @Valid RequestParameter para, BindingResult bindingResult) 
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
		return ResultUtil.success(list);
	}
}
