package com.example.account.dto;

import java.time.LocalDateTime;

import com.example.account.type.TransactionResultType;
import com.example.account.type.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfirmTransactionResponse {
	private String accountNumber;
	private TransactionType transactionType;
	private TransactionResultType transactionResultType;
	private String transactionId;
	private Long amount;
	private LocalDateTime transactedAt;
	
	public static ConfirmTransactionResponse from(TransactionDto transactionDto) {
		return ConfirmTransactionResponse.builder()
				.accountNumber(transactionDto.getAccountNumber())
				.transactionType(transactionDto.getTransactionType())
				.transactionResultType(transactionDto.getTransactionResultType())
				.transactionId(transactionDto.getTransactionId())
				.amount(transactionDto.getAmount())
				.transactedAt(transactionDto.getTransactedAt())
				.build();
	}
}
