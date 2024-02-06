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

    // sendMail 메서드 실행 전후에 로깅 어드바이스를 적용
    @Around("execution(* com.lumen.www.service.EmailService.sendMail(..))")
    public Object logMailSendExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis(); // 메서드 실행 전 현재 시간을 밀리초 단위로 측정

        try {
            return joinPoint.proceed(); // 실제 메서드를 실행하고 결과를 반환
        } finally {
            // 메서드 실행이 완료된 후 실행 시간을 측정
            long executionTime = System.currentTimeMillis() - startTime; // 시작 시간으로부터의 경과 시간 계산
            // 실행 시간을 로깅
            log.info("Execution of {} finished in {} ms", joinPoint.getSignature(), executionTime); // 메서드 시그니처와 실행 시간을 로그에 기록
        }
    }
}