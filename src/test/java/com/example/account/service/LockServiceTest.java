package com.example.account.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import com.example.account.exception.AccountException;
import com.example.account.type.ErrorCode;

@ExtendWith(MockitoExtension.class)
class LockServiceTest {
	@Mock
	private RedissonClient redissonClient;
	
	@Mock
	private RLock rLock;
	
	@InjectMocks
	private LockService lockService;
	
	@Test
	void getLocktest() throws InterruptedException {
		//given
		given(redissonClient.getLock(anyString()))
				.willReturn(rLock);
		given(rLock.tryLock(anyLong(), anyLong(), any()))
				.willReturn(true);
		
		//when
		//then
		assertDoesNotThrow(() -> lockService.lock("1000000010"));
	}
	
	@Test
	void failLocktest() throws InterruptedException {
		//given
		given(redissonClient.getLock(anyString()))
				.willReturn(rLock);
		given(rLock.tryLock(anyLong(), anyLong(), any()))
				.willReturn(false);
		
		//when
		AccountException accountException =
				assertThrows(AccountException.class, () -> lockService.lock("1000000010"));
		
		//then
		assertEquals(ErrorCode.LOCK, accountException.getErrorCode());
	}

}
