package com.cellinfo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Administrator
 *
 */
@Component
@Order(value=1)
public class InitBeanPostProcessor implements CommandLineRunner {

	@Autowired
	private SysEnviService sysEnviService;

	@Override
	public void run(String... args) throws Exception {

			System.out.println("================onApplicationEvent ContextRefreshedEvent====================");

			sysEnviService.loadInitData();



	}

}