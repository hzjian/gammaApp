package com.cellinfo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
public class ActivitiAppWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter{
	
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/dist/assets/");
		
		registry.addResourceHandler("/dist/**").addResourceLocations("classpath:/dist/");
		
		registry.addResourceHandler("/**").addResourceLocations("classpath:/dist/");
		super.addResourceHandlers(registry);
	}
	
	@Bean
	public InternalResourceViewResolver internalViewResover()
	{
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		
		viewResolver.setPrefix("/dist/");
		viewResolver.setSuffix(".html");
		viewResolver.setOrder(2);
		return viewResolver;
		
	}

	/*
	@Override  
    public void addCorsMappings(CorsRegistry registry) {  
        registry.addMapping("/**")  
                .allowedOrigins("*")  
                .allowCredentials(true)  
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .allowedHeaders("*")
                .maxAge(3600);
    }
    */
}
