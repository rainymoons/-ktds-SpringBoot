package com.ktdsuniversity.edu.hello_spring.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect // AOP 컴포넌트로 명시
@Component
public class TimingAspect {
	
	// AOP가 개입할 클래스 및 메소드를 명시.
	/**
	 * 어떤 관점으로 공통 코드를 넣고 싶은가?
	 * - public 모든 반환 타입 com.ktdsuniversity.edu.hello_spring 밑에 모든 패키지 중 service 패키지
	 * 	 내부의 모든 패키지에서 클래스의 이름이 ServiceImpl로 끝나는 모든 클래스의 모든 메소드를 대상으로 한다.
	 */
	@Pointcut("execution(public * com.ktdsuniversity.edu.hello_spring..service..*ServiceImpl.*(..))")
	public void aroundTarget() {
		// 아무것도 하지 않음.(코드품질검사 회피)
	}
	
	private static final Logger logger = LoggerFactory.getLogger(TimingAspect.class); 
	
	/**
	 * pointcut(aroundTarget())으로 지정한 클래스.메소드가 실행될 때,
	 * 공통으로 실행시킬 코드를 명시한다.
	 * @param pjp : 원래 코드(클래스와 메소드의 정보를 포함한다)
	 * @return (반환타입) : 원래 코드가 반환시킨 데이터. (컨트롤러에게 반환된다 : AOP가 개입할 클래스가 ServiceImpl이기 때문)
	 * @throws Throwable 
	 */
	// @Before @After @AfterThrowing -> 세개 다 포함하는 것이 @Around
	@Around("aroundTarget()") // aroundTarget에 달려 있는 pointCut을 대상으로 한다.
	public Object timingAdvice(ProceedingJoinPoint pjp) throws Throwable {
		
		Object result = null;
		
		// 현재 시간을 구한다.
		long currentTime = System.currentTimeMillis();
		
		// 원래 코드를 동작시킨다. 
		try {
			result = pjp.proceed();
		} catch (Throwable e) {
			// 개발자들에게 예외의 내용을 메일로 전송한다.
			throw e; // 그냥 던지기만 함.
		} finally {
			// 원래 코드가 실행되고나서부터 종료될 때 까지의 시간을 구한다.
			long endTime = System.currentTimeMillis();
			long proceedTime = endTime - currentTime;
			
			// 원래 실행되어야 할(혹은 원래 실행됬던) 클래스의 이름과 메소드의 이름을 추출한다.
			// 클래스의 패키지명을 포함한 클래스의 이름을 추출한다.
			String className = pjp.getTarget().getClass().getName();
			
			// 메소드의 이름을 추출한다.
			String methodName = pjp.getSignature().getName();
			
			if (logger.isInfoEnabled()) {
				logger.info("{}.{} 걸린시간 : {}ms ", className, methodName, proceedTime);
			}
		}
		return result;
	}

}
