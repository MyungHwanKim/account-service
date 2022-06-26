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
	MAX_ACCOUNT_COUNT_10("�ִ� ���� ���� 10�� �Դϴ�."),
	
	EXCEED_THAN_BALANCE("�ŷ��ݾ��� �ܾ׺��� Ů�ϴ�."),
	MIN_MAX_AMOUNT_UNMATCH("�ŷ��ݾ��� �ʹ� �۰ų� Ů�ϴ�.");
	
	private final String description;
}
