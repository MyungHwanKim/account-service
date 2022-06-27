package com.example.account.service;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import com.example.account.exception.AccountException;
import com.example.account.type.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LockService {
	private final RedissonClient redissonClient;
	
	public void lock(String accountNumber) {
		RLock lock = redissonClient.getLock(accountNumber);
		
		try {
			boolean isLock = lock.tryLock(1, 10, TimeUnit.SECONDS);
			if (!isLock) {
				log.error("=====Lock acquisition failed=====");
				throw new AccountException(ErrorCode.LOCK);
			}
		} catch (AccountException e) {
			throw e;
		} catch (Exception e) {
			log.error("Redis lock failed", e);
		}
	}
	
	public void unLock(String accountNumber) {
		redissonClient.getLock(accountNumber).unlock();
	}
}
