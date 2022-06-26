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
	MIN_MAX_AMOUNT_UNMATCH("�ŷ��ݾ��� �ʹ� �۰ų� Ů�ϴ�."),
	TRANSACTION_NOT_FOUND("�ش� �ŷ��� �����ϴ�."),
	INVALID_REQUEST("�߸��� ��û�Դϴ�."),
	TRANSACTION_ACCOUNT_UNMATCH("�ŷ��� ���°� ��ġ���� �ʽ��ϴ�."),
	CANCEL_AMOUNT_TRANSACTION_AMOUNT_NUMATCH("�ŷ��ݾװ� ��ұݾ��� ��ġ���� �ʽ��ϴ�."),
	AFTER_ONEYEAR_TRANSACTION("�ŷ��� �� 1���� �Ѿ����ϴ�.");
	
	private final String description;
}
