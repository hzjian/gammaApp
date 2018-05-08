package com.cellinfo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cellinfo.annotation.ServiceLog;
import com.cellinfo.service.UtilService;

@ServiceLog(moduleName = "业务数据操作")
@PreAuthorize("hasRole('ROLE_ADMIN')")  
@RestController
@RequestMapping("/service/busdata1")
public class SysDataProxyController {

	private final static Logger logger = LoggerFactory.getLogger(SysTaskProxyController.class);
	
	@Autowired
	private UtilService utilService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	
}
