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
import com.google.common.base.Objects;

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
				.transactionResultType(transactionResultType)
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
	
	@Transactional
	public TransactionDto cancelBalance(String transactionId, String accountNumber, Long amount) {
		Transaction transaction = transactionRepository.findByTransactionId(transactionId)
				.orElseThrow(() -> new AccountException(ErrorCode.TRANSACTION_NOT_FOUND));
		
		Account account = accountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));
		
		validateCancelBalance(transaction, account, amount);
		
		account.cancelBalance(amount);
		
		return TransactionDto.from(
				saveSuccessUseTransaction(TransactionType.CANCEL, 
						TransactionResultType.SUCCESS, account, amount));
	}
	
	public void validateCancelBalance(Transaction transaction, Account account, Long amount) {
		if(transaction.getAccount().getId() != account.getId()) {
			throw new AccountException(ErrorCode.TRANSACTION_ACCOUNT_UNMATCH);
		}
		
		if(!Objects.equal(transaction.getAmount(), amount)) {
			throw new AccountException(ErrorCode.CANCEL_AMOUNT_TRANSACTION_AMOUNT_NUMATCH);
		}
		
		if (transaction.getTransactedAt().isBefore(LocalDateTime.now().minusYears(1))) {
			throw new AccountException(ErrorCode.AFTER_ONEYEAR_TRANSACTION);
		}
	}
	
	public void saveFailedCancelTransaction(String accountNumber, Long amount) {
		Account account = accountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));
		
		saveSuccessUseTransaction(TransactionType.CANCEL, 
				TransactionResultType.FAIL, account, amount);
	}
	
	@Transactional
	public TransactionDto ConfirmTransaction(String transactionId) {
		return TransactionDto.from(transactionRepository.findByTransactionId(transactionId)
				.orElseThrow(() -> new AccountException(ErrorCode.TRANSACTION_NOT_FOUND)));
	}
}
