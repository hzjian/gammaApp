package com.cellinfo;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestUser {

	private BCryptPasswordEncoder  encoder =new BCryptPasswordEncoder();
	
	@Test
	public void getuserPassword()
	{
		String username = "admin";
		String password = "admin";
		
		System.out.println(encoder.encode(password));
		//$2a$10$04H6/ZGEDij7LMnTVwrqler7pZpIZ92dK4h4HluDr.quI14Gyuhu.
		//$2a$10$GljAt1ji64tDBExymBzQmuSwFCQiLXlfowXa7MkTZKjl3d/o07FXS
		//$2a$10$YvnEUeVIgCPRqwhUnvHeCOLYtr7hf/SfSmGzYvLOWeyuRAUmq9NVq

		
		
	}
}
