package com.lumen.www.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    // 모든 컨트롤러의 메서드 실행 전에 로그 기록
    @Before("execution(* com.lumen.www.controller.*.*(..))")
    public void logBeforeMethod(JoinPoint joinPoint) {
        log.info("Start: {} with arguments {}",
                joinPoint.getSignature().toShortString(),
                joinPoint.getArgs());
    }

    // 메서드가 성공적으로 반환된 후 로그 기록
    @AfterReturning("execution(* com.lumen.www.controller.*.*(..))")
    public void logAfterReturningMethod(JoinPoint joinPoint) {
        log.info("End: {}",
                joinPoint.getSignature().toShortString());
    }

  /*  // 메서드 실행 후, 반환값과 관계없이 로그 기록 (선택적)
    @After("execution(* com.lumen.www.controller.*.*(..))")
    public void logAfterMethodRegardless(JoinPoint joinPoint) {
        log.info("Completed: {}", joinPoint.getSignature().toShortString());
    }*/

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
