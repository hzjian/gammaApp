package com.cellinfo.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cellinfo.annotation.ServiceLog;
import com.cellinfo.entity.Result;
import com.cellinfo.entity.TlGammaGroup;
import com.cellinfo.security.UserInfo;
import com.cellinfo.service.SysEnviService;
import com.cellinfo.service.UtilService;
import com.cellinfo.utils.ResultUtil;

@ServiceLog(moduleName = "任务操作")
@PreAuthorize("hasRole('ROLE_ADMIN')")  
@RestController
@RequestMapping("/service/task1")
public class SysTaskProxyController {


	private final static Logger logger = LoggerFactory.getLogger(SysTaskProxyController.class);
	
	@Autowired
	private UtilService utilService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private SysEnviService sysEnviService;
	
	@PostMapping(value = "/initTaskData")
	public Result<Map<String,Object>> initTaskData(HttpServletRequest request) {
		Map<String,Object> resList = new HashMap<String ,Object>();
		
		UserInfo cUser = this.utilService.getCurrentUser(request);
		
		TlGammaGroup group = this.sysEnviService.getGroupById(cUser.getGroupGuid());
		
//		ResponseEntity<String> tmpStr = restTemplate.postForEntity(reqStr, para, String.class);
//		System.out.println(tmpStr.getBody());
//		return tmpStr.getBody();
		
		//restTemplate.postForEntity(group.getGroupService()+"", request, responseType)
		
		
		return ResultUtil.success(resList);
	}
	
}
