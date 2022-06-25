package com.example.account.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.account.domain.Account;
import com.example.account.domain.AccountUser;
import com.example.account.dto.AccountDto;
import com.example.account.exception.AccountException;
import com.example.account.repository.AccountRepository;
import com.example.account.repository.AccountUserRepository;
import com.example.account.type.AccountStatus;
import com.example.account.type.ErrorCode;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
	
	@Mock
	private AccountRepository accountRepository;
	
	@Mock
	private AccountUserRepository accountUserRepository;
	
	@InjectMocks
	private AccountService accountService;
	
	@Test
	@DisplayName("������ �ٸ� ���°� ���� ��� - ���� ���� ����")
	void createAccountTest() {
		//given
		AccountUser choi = AccountUser.builder()
				.id(2L)
				.name("�ֿ�")
				.build();
		
		given(accountUserRepository.findById(anyLong()))
				.willReturn(Optional.of(choi));
		given(accountRepository.findFirstByOrderByIdDesc())
				.willReturn(Optional.of(Account.builder()
						.accountNumber("1000000010")
						.balance(1000L).build()));
		given(accountRepository.save(any()))
				.willReturn(Account.builder()
						.accountUser(choi)
						.balance(1000L)
						.accountNumber("1000000011")
						.build());
		
		//when
		AccountDto accountDto = accountService.createAccount(1L, 1000L);
		
		//then
		assertEquals(2L, accountDto.getUserId());
		assertEquals("1000000011", accountDto.getAccountNumber());
		assertEquals(1000L, accountDto.getBalance());
	}
	
	
	@Test
	@DisplayName("ó�� ���¸� ����� ��� - ���� ���� ����")
	void createFirstAccountTest() {
		//given
		AccountUser choi = AccountUser.builder()
				.id(2L)
				.name("�ֿ�")
				.build();
		
		given(accountUserRepository.findById(anyLong()))
				.willReturn(Optional.of(choi));
		given(accountRepository.findFirstByOrderByIdDesc())
				.willReturn(Optional.empty());
		given(accountRepository.save(any()))
				.willReturn(Account.builder()
						.accountUser(choi)
						.accountNumber("1000000012")
						.build());
		
		//when
		AccountDto accountDto = accountService.createAccount(1L, 1000L);
		
		//then
		assertEquals(2L, accountDto.getUserId());
	}
	
	@Test
	@DisplayName("����ڰ� ���� ��� - ���� ���� ����")
	void userNotFoundTest() {
		//given
		given(accountUserRepository.findById(anyLong()))
				.willReturn(Optional.empty());
		
		//when
		AccountException accountException = assertThrows(AccountException.class,
				() -> accountService.createAccount(1L, 1000L));
		
		//then
		assertEquals(ErrorCode.USER_NOT_FOUND, accountException.getErrorCode());
	}

	@Test
	@DisplayName("���� ���� 10�� ��� - ���� ���� ����")
	void max_Account_Count_10_Test() {
		//given
		AccountUser choi = AccountUser.builder()
				.id(2L)
				.name("�ֿ�")
				.build();
		given(accountUserRepository.findById(anyLong()))
		.willReturn(Optional.of(choi));
		given(accountRepository.countByAccountUser(any()))
				.willReturn(10);
		
		//when
		AccountException accountException = assertThrows(AccountException.class,
				() -> accountService.createAccount(1L, 1000L));
		
		//then
		assertEquals(ErrorCode.MAX_ACCOUNT_COUNT_10, accountException.getErrorCode());
	}
	
	@Test
	@DisplayName("���¸� �����ϴ� ��� - ���� ���� ����")
	void deleteAccountTest() {
		//given
		AccountUser choi = AccountUser.builder()
				.id(2L)
				.name("�ֿ�")
				.build();
		
		given(accountUserRepository.findById(anyLong()))
				.willReturn(Optional.of(choi));
		given(accountRepository.findByAccountNumber(anyString()))
				.willReturn(Optional.of(Account.builder()
						.accountUser(choi)
						.balance(0L)
						.accountNumber("1000000011")
						.build()));
				
		
		//when
		AccountDto accountDto = accountService.deleteAccount(1L, "1000000000");
		
		//then
		assertEquals(2L, accountDto.getUserId());
		assertEquals("1000000011", accountDto.getAccountNumber());
		assertEquals(0L, accountDto.getBalance());
	}
	
	@Test
	@DisplayName("����ڰ� ���� ��� - ���� ���� ����")
	void deleteUserNotFoundTest() {
		//given
		given(accountUserRepository.findById(anyLong()))
				.willReturn(Optional.empty());
		
		//when
		AccountException accountException = assertThrows(AccountException.class,
				() -> accountService.deleteAccount(1L, "1000000000"));
		
		//then
		assertEquals(ErrorCode.USER_NOT_FOUND, accountException.getErrorCode());
	}
	
	@Test
	@DisplayName("���°� ���� ��� - ���� ���� ����")
	void deleteAccountNotFoundTest() {
		//given
		AccountUser choi = AccountUser.builder()
				.id(2L)
				.name("�ֿ�")
				.build();
		
		given(accountUserRepository.findById(anyLong()))
				.willReturn(Optional.of(choi));
		given(accountRepository.findByAccountNumber(anyString()))
				.willReturn(Optional.empty());
		
		//when
		AccountException accountException = assertThrows(AccountException.class,
				() -> accountService.deleteAccount(1L, "1000000000"));
		
		//then
		assertEquals(ErrorCode.ACCOUNT_NOT_FOUND, accountException.getErrorCode());
	}
	
	@Test
	@DisplayName("����ڿ� ���� �����ְ� ��ġ���� �ʴ� ��� - ���� ���� ����")
	void deleteAccountNotUserUnMatchTest() {
		//given
		AccountUser choi = AccountUser.builder()
				.id(1L)
				.name("�ֿ�")
				.build();
		AccountUser guk = AccountUser.builder()
				.id(2L)
				.name("������")
				.build();
		
		given(accountUserRepository.findById(anyLong()))
				.willReturn(Optional.of(choi));
		given(accountRepository.findByAccountNumber(anyString()))
				.willReturn(Optional.of(Account.builder()
						.accountUser(guk)
						.accountNumber("1000000001")
						.balance(0L)
						.build()));
		
		//when
		AccountException accountException = assertThrows(AccountException.class,
				() -> accountService.deleteAccount(1L, "1000000000"));
		
		//then
		assertEquals(ErrorCode.USER_ACCOUNT_UNMATCH, accountException.getErrorCode());
	}
	
	@Test
	@DisplayName("���°� �̹� ������ ��� - ���� ���� ����")
	void deleteAccountAlreadyUnregisteredTest() {
		//given
		AccountUser choi = AccountUser.builder()
				.id(2L)
				.name("�ֿ�")
				.build();
		
		given(accountUserRepository.findById(anyLong()))
				.willReturn(Optional.of(choi));
		given(accountRepository.findByAccountNumber(anyString()))
				.willReturn(Optional.of(Account.builder()
						.accountUser(choi)
						.accountNumber("1000000001")
						.accountStatus(AccountStatus.UNREGISTERED)
						.balance(0L)
						.build()));
		
		//when
		AccountException accountException = assertThrows(AccountException.class,
				() -> accountService.deleteAccount(1L, "1000000000"));
		
		//then
		assertEquals(ErrorCode.ACCOUNT_ALREADY_UNREGISTERED, accountException.getErrorCode());
	}
	
	@Test
	@DisplayName("�ܾ��� �ִ� ��� - ���� ���� ����")
	void deleteAccountBalanceNotZeroTest() {
		//given
		AccountUser choi = AccountUser.builder()
				.id(2L)
				.name("�ֿ�")
				.build();
		
		given(accountUserRepository.findById(anyLong()))
				.willReturn(Optional.of(choi));
		given(accountRepository.findByAccountNumber(anyString()))
				.willReturn(Optional.of(Account.builder()
						.accountUser(choi)
						.accountNumber("1000000001")
						.balance(1000L)
						.build()));
		
		//when
		AccountException accountException = assertThrows(AccountException.class,
				() -> accountService.deleteAccount(1L, "1000000000"));
		
		//then
		assertEquals(ErrorCode.BALANCE_NOT_ZERO, accountException.getErrorCode());
	}
}
