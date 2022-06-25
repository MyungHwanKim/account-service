package com.example.account.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	USER_NOT_FOUND("사용자가 없습니다."),
	MAX_ACCOUNT_COUNT_10("최대 계좌 수는 10개 입니다.");
	
	private final String description;
}
