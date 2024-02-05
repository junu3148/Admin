package com.lumen.www.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class PerformanceLoggingAspect {

    // 모든 컨트롤러의 메서드 실행 전후에 로그 기록 및 소요 시간 계산
    @Around("execution(* com.lumen.www.controller.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis(); // 메서드 실행 전 시간 측정
        try {
            return joinPoint.proceed();
        } finally {
            long executionTime = System.currentTimeMillis() - start; // 메서드 실행 후 시간 측정 및 소요 시간 계산
            log.info("Completed: {} with arguments {} in {} ms",
                    joinPoint.getSignature().toShortString(),
                    joinPoint.getArgs(),
                    executionTime); // 로그 기록
        }
    }
}