package com.example.account.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	USER_NOT_FOUND("����ڰ� �����ϴ�."),
	MAX_ACCOUNT_COUNT_10("�ִ� ���� ���� 10�� �Դϴ�.");
	
	private final String description;
}
