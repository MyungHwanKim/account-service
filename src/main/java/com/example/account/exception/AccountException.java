package com.example.account.exception;

import com.example.account.type.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountException extends RuntimeException{
	private ErrorCode errorCode;
	private String errorDescription;
	
	public AccountException(ErrorCode errorCode) {
		this.errorCode = errorCode;
		this.errorDescription = errorCode.getDescription();
	}
}
