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

    /**
     * 컨트롤러 메소드 실행 전에 로그를 기록합니다.
     * <p>
     * com.lumen.www.controller 패키지 내의 모든 클래스의 모든 메소드 실행 전에 호출됩니다.
     * 실행되는 메소드의 이름과 인자를 로깅하여, 어떤 메소드가 호출되었는지와 전달된 인자가 무엇인지를 기록합니다.
     * </p>
     *
     * @param joinPoint 조인 포인트 정보를 제공합니다. 실행되는 메소드에 대한 정보를 포함합니다.
     */
    @Before("execution(* com.lumen.www.controller.*.*(..))")
    public void logBeforeMethod(JoinPoint joinPoint) {
        log.info("Start: {} with arguments {}",
                joinPoint.getSignature().toShortString(),
                joinPoint.getArgs());
    }

    /**
     * 컨트롤러 메소드의 실행이 성공적으로 반환된 후 로그를 기록합니다.
     * <p>
     * com.lumen.www.controller 패키지 내의 모든 클래스의 모든 메소드 실행이 성공적으로 완료되고 반환될 때 호출됩니다.
     * 실행되는 메소드의 이름을 로깅하여, 어떤 메소드가 성공적으로 완료되었는지를 기록합니다.
     * </p>
     *
     * @param joinPoint 조인 포인트 정보를 제공합니다. 실행되는 메소드에 대한 정보를 포함합니다.
     */
    @AfterReturning("execution(* com.lumen.www.controller.*.*(..))")
    public void logAfterReturningMethod(JoinPoint joinPoint) {
        log.info("End: {}",
                joinPoint.getSignature().toShortString());
    }

    /**
     * 컨트롤러 메소드의 실행 시간을 로깅합니다.
     * <p>
     * com.lumen.www.controller 패키지 내의 모든 클래스의 모든 메소드 실행 시간을 측정하고 로깅합니다.
     * 메소드의 시작과 끝 시간을 기록하여, 실행 시간을 밀리초 단위로 계산하고 기록합니다.
     * 이를 통해 시스템의 성능 분석 및 모니터링에 유용한 데이터를 제공합니다.
     * </p>
     *
     * @param joinPoint 진행 중인 조인 포인트의 정보를 제공합니다. 실행 중인 메소드에 대한 정보를 포함합니다.
     * @return 실행 메소드의 결과 객체를 반환합니다.
     * @throws Throwable 실행 중인 메소드에서 발생할 수 있는 모든 예외를 던집니다.
     */
    @Around("execution(* com.lumen.www.controller.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long executionTime = System.currentTimeMillis() - start;
            log.info("Completed: {} with arguments {} in {} ms",
                    joinPoint.getSignature().toShortString(),
                    joinPoint.getArgs(),
                    executionTime);
        }
    }
}
