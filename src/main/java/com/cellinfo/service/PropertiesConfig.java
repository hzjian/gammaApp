package com.cellinfo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.cellinfo.controller.entity.PropsQueryParameter;

@Component
@ConfigurationProperties(prefix = "com.gamma")
@PropertySource(value = "classpath:config.properties",encoding="UTF-8")
public class PropertiesConfig {

    private  List<PropsQueryParameter> kernel = new ArrayList<PropsQueryParameter>();

	/**
	 * @return the kernel
	 */
	public List<PropsQueryParameter> getKernel() {
		return kernel;
	}

	/**
	 * @param kernel the kernel to set
	 */
	public void setKernel(List<PropsQueryParameter> kernel) {
		this.kernel = kernel;
	}
    
    
    
}
