package com.example.account.dto;

import java.time.LocalDateTime;

import com.example.account.domain.Transaction;
import com.example.account.type.TransactionResultType;
import com.example.account.type.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TransactionDto {
	private String accountNumber;
	private TransactionType transactionType;
	private TransactionResultType transactionResultType;
	private Long amount;
	private Long balanceSnapshot;
	private String transactionId;
	private LocalDateTime transactedAt;
	
	public static TransactionDto from(Transaction transaction) {
		return TransactionDto.builder()
				.accountNumber(transaction.getAccount().getAccountNumber())
				.transactionType(transaction.getTransactionType())
				.transactionResultType(transaction.getTransactionResultType())
				.amount(transaction.getAmount())
				.balanceSnapshot(transaction.getBalanceSnapshot())
				.transactionId(transaction.getTransactionId())
				.transactedAt(transaction.getTransactedAt())
				.build();
	}
}
