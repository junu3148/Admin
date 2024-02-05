package com.lumen.www.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class MailPerformanceLoggingAspect {

    @Around("execution(* com.lumen.www.service.EmailService.sendMail(..))")
    public Object logMailSendExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis(); // 메서드 실행 전 시간 측정
        try {
            return joinPoint.proceed();
        } finally {
            long executionTime = System.currentTimeMillis() - startTime; // 메서드 실행 후 시간 측정
            log.info("Execution of {} finished in {} ms", joinPoint.getSignature(), executionTime); // 실행 시간 로그 기록
        }
    }
}



