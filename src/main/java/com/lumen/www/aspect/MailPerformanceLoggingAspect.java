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

    /**
     * 이메일 발송 작업의 실행 시간을 로깅하는 Around 어드바이스입니다.
     * <p>
     * {@code com.lumen.www.service.EmailService} 내의 "sendMail"로 시작하는 모든 메소드를 가로채어
     * 해당 메소드의 실행 시간을 로깅합니다. 이는 모니터링 및 성능 분석에 유용합니다.
     * </p>
     *
     * @param joinPoint 메소드 실행에 대한 조인 포인트입니다. 실행 중인 메소드에 대한 정보를 제공합니다.
     * @return 실행 메소드의 결과 객체를 반환합니다.
     * @throws Throwable 실행 중인 메소드에서 발생할 수 있는 모든 예외를 던집니다.
     * @since 1.x (클래스 버전 정보를 명시하세요)
     */
    @Around("execution(* com.lumen.www.service.EmailService.sendMail*(..))")
    public Object logMailSendExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {

            long executionTime = System.currentTimeMillis() - startTime;
            log.info("Execution of {} finished in {} ms", joinPoint.getSignature(), executionTime);
        }
    }
}