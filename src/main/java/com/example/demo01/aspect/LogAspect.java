package com.example.demo01.aspect;

import com.example.demo01.annotation.Log;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

/**
 * 日志切面
 */
@Aspect
@Component
@Slf4j
public class LogAspect {
    //日志切入点
    @Pointcut("@annotation(com.example.demo01.annotation.Log)")
    public void logPointcut(){
    }

    @AfterReturning(pointcut = "@annotation(controllerLog)",returning = "jsonResult")
    public void doAfter(JoinPoint joinPoint, Log controllerLog,Object jsonResult){

    }
    protected void handleLog(JoinPoint joinPoint, Log controllerLog,Exception e,Object jsonResult){

    }

}
