package com.example.account.service;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.account.domain.Account;
import com.example.account.domain.AccountUser;
import com.example.account.dto.AccountDto;
import com.example.account.exception.AccountException;
import com.example.account.repository.AccountRepository;
import com.example.account.repository.AccountUserRepository;
import com.example.account.type.AccountStatus;
import com.example.account.type.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
	private final AccountRepository accountRepository;
	private final AccountUserRepository accountUserRepository;
	
	@Transactional
	public AccountDto createAccount(Long userId, Long initBalance) {
		AccountUser accountUser = accountUserRepository.findById(userId)
				.orElseThrow(() -> new AccountException(ErrorCode.USER_NOT_FOUND));
		
		if (accountRepository.countByAccountUser(accountUser) >= 10) {
			throw new AccountException(ErrorCode.MAX_ACCOUNT_COUNT_10);
		}
		
		String newAccountNumber = accountRepository.findFirstByOrderByIdDesc()
				.map(account -> (Integer.parseInt(account.getAccountNumber())) + 1 + "")
				.orElse("1000000000");
		
		return AccountDto.from(
				accountRepository.save(Account.builder()
						.accountUser(accountUser)
						.accountNumber(newAccountNumber)
						.accountStatus(AccountStatus.IN_USE)
						.balance(initBalance)
						.registeredAt(LocalDateTime.now())
						.build()));
	}
	
	@Transactional
	public AccountDto deleteAccount(Long userId, String accountNumber) {
		AccountUser accountUser = accountUserRepository.findById(userId)
				.orElseThrow(() -> new AccountException(ErrorCode.USER_NOT_FOUND));
		
		Account account = accountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));
		
		validateDeleteAccount(accountUser, account);
		
		account.setAccountStatus(AccountStatus.UNREGISTERED);
		account.setUnregisteredAt(LocalDateTime.now());
		
		return AccountDto.from(account);
	}
	
	public void validateDeleteAccount(AccountUser accountUser, Account account) {
		if (accountUser.getId() != account.getAccountUser().getId()) {
			throw new AccountException(ErrorCode.USER_ACCOUNT_UNMATCH);
		}
		
		if (account.getAccountStatus() == AccountStatus.UNREGISTERED) {
			throw new AccountException(ErrorCode.ACCOUNT_ALREADY_UNREGISTERED);
		}
		
		if (account.getBalance() > 0) {
			throw new AccountException(ErrorCode.BALANCE_NOT_ZERO);
		}
	}
}
