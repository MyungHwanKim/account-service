package com.example.account.service;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.account.domain.Account;
import com.example.account.domain.AccountUser;
import com.example.account.domain.Transaction;
import com.example.account.dto.TransactionDto;
import com.example.account.exception.AccountException;
import com.example.account.repository.AccountRepository;
import com.example.account.repository.AccountUserRepository;
import com.example.account.repository.TransactionRepository;
import com.example.account.type.AccountStatus;
import com.example.account.type.ErrorCode;
import com.example.account.type.TransactionResultType;
import com.example.account.type.TransactionType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor	
public class TransactionService {
	private final AccountRepository accountRepository;
	private final AccountUserRepository accountUserRepository;
	private final TransactionRepository transactionRepository;
	
	
	@Transactional
	public TransactionDto useBalance(Long userId, String accountNumber, Long amount) {
		AccountUser accountUser = accountUserRepository.findById(userId)
				.orElseThrow(() -> new AccountException(ErrorCode.USER_NOT_FOUND));
		Account account = accountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));
		
		validateUseBalance(accountUser, account, amount);
		
		account.useBalance(amount);
		
		return TransactionDto.from(
				saveSuccessUseTransaction(TransactionType.USE, 
						TransactionResultType.SUCCESS, account, amount));
	}
	
	public void validateUseBalance(AccountUser accountUser, Account account, Long amount) {
		if (accountUser.getId() != account.getAccountUser().getId()) {
			throw new AccountException(ErrorCode.USER_ACCOUNT_UNMATCH);
		}
		
		if (account.getAccountStatus() == AccountStatus.UNREGISTERED) {
			throw new AccountException(ErrorCode.ACCOUNT_ALREADY_UNREGISTERED);
		}
		
		if (account.getBalance() < amount) {
			throw new AccountException(ErrorCode.EXCEED_THAN_BALANCE);
		}
		
		if (amount < 10 || amount > 10_000_000) {
			throw new AccountException(ErrorCode.MIN_MAX_AMOUNT_UNMATCH);
		}
	}
	
	public Transaction saveSuccessUseTransaction(
			TransactionType transactionType, 
			TransactionResultType transactionResultType,
			Account account, Long amount) 
	{
		return transactionRepository.save(Transaction.builder()
				.transactionType(transactionType)
				.tranactionResultType(transactionResultType)
				.account(account)
				.amount(amount)
				.balanceSnapshot(account.getBalance())
				.transactionId(UUID.randomUUID().toString().replace("-", ""))
				.transactedAt(LocalDateTime.now())
				.build());
	}
	
	public void saveFailedUseTransaction(String accountNumber, Long amount) {
		Account account = accountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));
		
		saveSuccessUseTransaction(TransactionType.USE, 
				TransactionResultType.FAIL, account, amount);
	}
}
