package com.cellinfo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import com.cellinfo.filters.JwtAuthenticationTokenFilter;
import com.cellinfo.filters.StatelessLoginFilter;
import com.cellinfo.security.TokenAuthenticationService;
import com.cellinfo.service.SysLogService;
import com.cellinfo.service.SysUserService;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
	
	@Value("${gammasecurity.enabled:true}")
	private boolean securityEnabled;
	
	@Autowired  
    private SysUserService userService;
	
	@Autowired
    private SysLogService sysLogService;
	
	@Autowired
	private TokenAuthenticationService tokenAuthenticationService;

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
	   // Do any additional configuration here
	   return builder.build();
	}
	
	@Bean
	public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }

//	@Override
//    public void configure(WebSecurity web) throws Exception {
//        if (!securityEnabled)
//            web.ignoring().antMatchers("/**");
//    }

	
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        
    	httpSecurity
    	// .cors().and()
        // 由于使用的是JWT，我们这里不需要csrf,csrf又称跨域请求伪造，攻击方通过伪造用户请求访问受信任站点
        .csrf().disable()
        .headers().frameOptions().disable()
        .and()
        .exceptionHandling()
        .and()
	        // 基于token，所以不需要session
	        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
	        .authorizeRequests()
	        // 允许对于网站静态资源的无授权访问
	        //.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()//就是这一行啦 
	        //配置Spring security策略，不拦截OPTIONS请求 跨域请求
	        //.antMatchers(HttpMethod.OPTIONS).permitAll() 
	        .antMatchers(
	                HttpMethod.GET,
	                "/","'/login/**",
	                "/dist/**",
	                "/assets/**",
	                "/*.html",
	                "/favicon.ico",
	                "/**/*.html",
	                "/**/*.css",
	                "/**/*.js",
	                "/**/*.json",
	                "/**/*.jpg",
	                "/**/*.png",
	                "/**/*.wof","/**/*.woff2",
	                "/**/*.ttf"
	        		)
	        .permitAll()
	        .antMatchers("/auth/login").permitAll()
	        //.antMatchers("/api/*/**").permitAll()
	    .anyRequest() 
        // 除上面外的所有请求全部需要鉴权认证
        .authenticated();

		// 禁用缓存
    	httpSecurity.headers().cacheControl();
    	
    	httpSecurity.addFilterBefore(
                new StatelessLoginFilter("/service/auth/login", tokenAuthenticationService, userService,sysLogService, authenticationManager()),
                UsernamePasswordAuthenticationFilter.class);
    	httpSecurity.addFilterBefore(authenticationTokenFilterBean(),  UsernamePasswordAuthenticationFilter.class);
    }
    
    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(this.userService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }
    
    @Override
    protected UserDetailsService userDetailsService() {
        return userService;
    }

}