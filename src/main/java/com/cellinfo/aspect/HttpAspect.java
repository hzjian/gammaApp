package com.cellinfo.aspect;

import java.lang.reflect.Method;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.cellinfo.annotation.OperLog;
import com.cellinfo.annotation.ServiceLog;
import com.cellinfo.entity.TlGammaLog;
import com.cellinfo.security.JwtTokenHandler;
import com.cellinfo.service.SysLogService;


@Aspect
@Component
public class HttpAspect {

    private final static Logger logger = LoggerFactory.getLogger(HttpAspect.class);
    ThreadLocal<Long> startTime = new ThreadLocal<>();  
    
    @Autowired
    private JwtTokenHandler jwtTokenHandler;

    @Value("${jwt.tokenName}")
    private String tokenName;
    
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private SysLogService sysLogService;

    @Pointcut("execution(public * com.cellinfo.controller.*.*(..))")
    public void log() {
    }

//    @Before("log()")
//    public void doBefore(JoinPoint joinPoint) {
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
//
//        //url
//        logger.info("url={}", request.getRequestURL());
//
//        //method
//        logger.info("method={}", request.getMethod());
//
//        //ip
//        logger.info("ip={}", request.getRemoteAddr());
//
//        //类方法
//        logger.info("class_method={}", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
//
//        //参数
//        logger.info("args={}", joinPoint.getArgs());
//    }

//    @After("log()")
//    public void doAfter() {
//        logger.info("222222222222");
//    }

//    @AfterReturning(returning = "object", pointcut = "log()")
//    public void doAfterReturning(Object object) {
//    	if(object != null)
//    		logger.info("response={}", object.toString());
//    }

    
	@Before("log()")  
    public void webCallBefore(JoinPoint joinPoint) throws Throwable {  
//        startTime.set(System.currentTimeMillis());  
//  
//        // 接收到请求，记录请求内容  
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();  
//        HttpServletRequest request = attributes.getRequest();  
//  
//        // 记录下请求内容  
//        logger.info("URL : " + request.getRequestURL().toString());  
//        logger.info("HTTP_METHOD : " + request.getMethod());  
//        logger.info("IP : " + request.getRemoteAddr());  
//        logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());  
//        logger.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));  
  
    }  
  
//    @AfterReturning(returning = "ret", pointcut = "log()")  
//    public void webCallAfterReturning(Object ret) throws Throwable {  
//         //处理完请求，返回内容  
//        logger.info("RESPONSE : " + ret);  
//        logger.info("SPEND TIME : " + (System.currentTimeMillis() - startTime.get()));  
//    }  
	// 方法执行的前后调用
	@Around("log()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		TlGammaLog syslog = new TlGammaLog();
		String servName = "";
		String funcName = "";
		String operDesc = "";
		String urlStr = "";
		String ipStr = "";
		String userName ="";

		try {
			MethodSignature joinPointObject = (MethodSignature) pjp.getSignature();
			Method method = joinPointObject.getMethod();
			// 获取执行方法的参数
			boolean flag = method.isAnnotationPresent(OperLog.class);
			if (flag) {
				boolean flag1 = pjp.getTarget().getClass()
						.isAnnotationPresent(ServiceLog.class);
				if (flag1) {
					ServiceLog servLog = (ServiceLog) pjp.getTarget()
							.getClass().getAnnotation(ServiceLog.class);
					if (servLog.moduleName() != null)
						servName = servLog.moduleName();
				}

				OperLog operAnno = method.getAnnotation(OperLog.class);
				if (operAnno.funcName() != null)
					funcName = operAnno.funcName();
				try{
					 ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
					 HttpServletRequest request = attributes.getRequest();
					 urlStr = request.getRequestURL().toString();
					 ipStr = request.getRemoteAddr();
	
					 String auth_token = request.getHeader(this.tokenName);
			         if (!StringUtils.isEmpty(auth_token) && auth_token.startsWith(this.tokenHead)) {
			            auth_token = auth_token.substring(this.tokenHead.length());
			            userName = jwtTokenHandler.getUsernameFromToken(auth_token);
			         }	
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				syslog.setModuleName(servName);
				syslog.setOprateName(funcName);
				
				syslog.setUserName(userName);
				syslog.setIpStr(ipStr);
				syslog.setUrlStr(urlStr);
				syslog.setLogTime(new Timestamp(System.currentTimeMillis()));			
			} else {
				logger.debug("不满足日志记录条件");
			}
		} catch (Exception e) {
			logger.error(e.toString());
			e.printStackTrace();
		}

		Object obj = pjp.proceed();
		
		try {
			HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
			operDesc += "output:"+response.getOutputStream().toString();
			if(operDesc.length()>1000)
				operDesc = 	operDesc.substring(0,1000);
			syslog.setOprateDesc(operDesc);
			sysLogService.save(syslog);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}

	// 方法运行出现异常时调用
	@AfterThrowing(pointcut = "execution (* com.bhz.cpms.service.impl.*.*(..))", throwing = "ex")
	public void afterThrowing(Exception ex) {
		System.out.println("afterThrowing");
		System.out.println(ex);
	}
}
