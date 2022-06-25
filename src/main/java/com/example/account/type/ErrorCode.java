package com.example.account.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	USER_NOT_FOUND("사용자가 없습니다."),
	ACCOUNT_NOT_FOUND("계좌가 없습니다."),
	USER_ACCOUNT_UNMATCH("사용자와 계좌 소유주가 일치하지 않습니다."),
	ACCOUNT_ALREADY_UNREGISTERED("계좌가 이미 해지 상태입니다."),
	BALANCE_NOT_ZERO("잔액이 남아있습니다."),
	MAX_ACCOUNT_COUNT_10("최대 계좌 수는 10개 입니다.");
	
	private final String description;
}
