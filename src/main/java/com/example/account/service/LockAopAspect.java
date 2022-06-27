package com.example.account.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.example.account.aop.AccountLockIdInterface;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class LockAopAspect {
	private final LockService lockService;
	
	@Around("@annotation(com.example.account.aop.AccountLock) && args(request)")
	public Object aroundMethod(
			ProceedingJoinPoint pjp, AccountLockIdInterface request) throws Throwable 
	{
		lockService.lock(request.getAccountNumber());
		try {	
			return pjp.proceed();
		} finally {
			lockService.unLock(request.getAccountNumber());
		}
	}
}
