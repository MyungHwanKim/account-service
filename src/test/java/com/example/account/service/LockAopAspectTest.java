package com.example.account.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.BDDMockito.given;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.account.dto.UseBalance;
import com.example.account.exception.AccountException;
import com.example.account.type.ErrorCode;


@ExtendWith(MockitoExtension.class)
class LockAopAspectTest {
	@Mock
	private LockService lockService;
	
	@Mock
	private ProceedingJoinPoint proceedingJoinPoint;
	
	@InjectMocks
	private LockAopAspect lockAopAspect;

	@Test
	void lockAndUnlocktest() throws Throwable {
		//given
		ArgumentCaptor<String> lockArgumentCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> unLockArgumentCaptor = ArgumentCaptor.forClass(String.class);
		UseBalance.Request request = new UseBalance.Request(1L, "1000000010", 1000L);
		
		//when
		lockAopAspect.aroundMethod(proceedingJoinPoint, request);
		
		//then
		verify(lockService, times(1)).lock(lockArgumentCaptor.capture());
		verify(lockService, times(1)).unLock(unLockArgumentCaptor.capture());
		
		assertEquals("1000000010", lockArgumentCaptor.getValue());
		assertEquals("1000000010", unLockArgumentCaptor.getValue());
	}

	@Test
	void lockAndUnlock_evenIfThrowtest() throws Throwable {
		//given
		ArgumentCaptor<String> lockArgumentCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> unLockArgumentCaptor = ArgumentCaptor.forClass(String.class);
		UseBalance.Request request = new UseBalance.Request(1L, "1000000010", 1000L);
		
		given(proceedingJoinPoint.proceed())
				.willThrow(new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));
		
		//when
		assertThrows(AccountException.class, () -> 
		lockAopAspect.aroundMethod(proceedingJoinPoint, request));
		
		//then
		verify(lockService, times(1)).lock(lockArgumentCaptor.capture());
		verify(lockService, times(1)).unLock(unLockArgumentCaptor.capture());
		
		assertEquals("1000000010", lockArgumentCaptor.getValue());
		assertEquals("1000000010", unLockArgumentCaptor.getValue());
	}
}
