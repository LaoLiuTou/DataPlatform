package com.lt.aspect;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

@Component
@Aspect
public class TokenAspect {
	@Pointcut("execution(public * com.lt.controller.*.*(..))")
    public void checkToken(){
    }

    @Before("checkToken()")
    public void beforeCheckToken(){
    	
        System.out.println("调用方法之前。。。。");
    }

    @AfterReturning("checkToken()")
    public void afterCheckToken(){
        System.out.println("调用方法结束之后。。。。");
    }

    //抛出异常时才调用  
    @AfterThrowing("checkToken()")  
    public void afterThrowing()  
    {  
        System.out.println("校验token出现异常了......");  
    }
}
