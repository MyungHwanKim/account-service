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
	MAX_ACCOUNT_COUNT_10("최대 계좌 수는 10개 입니다."),
	
	EXCEED_THAN_BALANCE("거래금액이 잔액보다 큽니다."),
	MIN_MAX_AMOUNT_UNMATCH("거래금액이 너무 작거나 큽니다."),
	TRANSACTION_NOT_FOUND("해당 거래가 없습니다."),
	INVALID_REQUEST("잘못된 요청입니다."),
	TRANSACTION_ACCOUNT_UNMATCH("거래와 계좌가 일치하지 않습니다."),
	CANCEL_AMOUNT_TRANSACTION_AMOUNT_NUMATCH("거래금액과 취소금액이 일치하지 않습니다."),
	AFTER_ONEYEAR_TRANSACTION("거래한 지 1년이 넘었습니다.");
	
	private final String description;
}
