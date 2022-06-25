package com.example.account.dto;

import java.time.LocalDateTime;

import com.example.account.domain.Account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AccountDto {
	private Long userId;
	private String accountNumber;
	private Long balance;
	
	private LocalDateTime registeredAt;
	private LocalDateTime unregisteredAt;
	
	public static AccountDto from(Account account) {
		return AccountDto.builder()
				.userId(account.getAccountUser().getId())
				.accountNumber(account.getAccountNumber())
				.balance(account.getBalance())
				.registeredAt(account.getRegisteredAt())
				.unregisteredAt(account.getUnregisteredAt())
				.build();
	}
}
