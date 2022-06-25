package com.example.account.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	USER_NOT_FOUND("����ڰ� �����ϴ�."),
	ACCOUNT_NOT_FOUND("���°� �����ϴ�."),
	USER_ACCOUNT_UNMATCH("����ڿ� ���� �����ְ� ��ġ���� �ʽ��ϴ�."),
	ACCOUNT_ALREADY_UNREGISTERED("���°� �̹� ���� �����Դϴ�."),
	BALANCE_NOT_ZERO("�ܾ��� �����ֽ��ϴ�."),
	MAX_ACCOUNT_COUNT_10("�ִ� ���� ���� 10�� �Դϴ�.");
	
	private final String description;
}
