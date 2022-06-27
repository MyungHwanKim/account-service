package com.example.account.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.example.account.aop.AccountLockIdInterface;
import com.example.account.type.TransactionResultType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UseBalance {
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Request implements AccountLockIdInterface{
		@NotNull
		@Min(1)
		private Long userId;
		
		@NotBlank
		@Size(min = 10, max = 10)
		private String accountNumber;
		
		@NotNull
		@Min(10)
		@Max(10_000_000)
		private Long amount;
	}
	
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class Response {
		private String accountNumber;
		private TransactionResultType transactionResult;
		private String transactionId;
		private Long amount;
		private LocalDateTime transactedAt;
		
		public static Response from(TransactionDto transactionDto) {
			return Response.builder()
					.accountNumber(transactionDto.getAccountNumber())
					.transactionResult(transactionDto.getTransactionResultType())
					.transactionId(transactionDto.getTransactionId())
					.amount(transactionDto.getAmount())
					.transactedAt(transactionDto.getTransactedAt())
					.build();
		}
	}
}
