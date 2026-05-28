package com.btvn.employeemanagement.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Before("execution(* com.btvn.employeemanagement.controller.*.*(..))")
    public void logBeforeController(JoinPoint joinPoint) {
        logger.info(">>> LOGGER : AOP Before: Method {} is being called", joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "execution(* com.btvn.employeemanagement.service.*.*(..))", returning = "result")
    public void logAfterService(JoinPoint joinPoint, Object result) {
        logger.info(">>> LOGGER : AOP AfterReturning: Method {} returned value: {}",
                joinPoint.getSignature().getName(), result != null ? result.toString() : "null");
    }

    @Around("execution(* com.btvn.employeemanagement.controller.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        logger.info(">>> LOGGER : AOP Around: {} executed in {}ms", joinPoint.getSignature(), executionTime);
        return proceed;
    }
}