package com.example.account.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
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
	@DisplayName("이전의 다른 계좌가 있을 경우 - 계좌 생성 성공")
	void createAccountTest() {
		//given
		AccountUser choi = AccountUser.builder()
				.id(2L)
				.name("최웅")
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
	@DisplayName("처음 계좌를 만드는 경우 - 계좌 생성 성공")
	void createFirstAccountTest() {
		//given
		AccountUser choi = AccountUser.builder()
				.id(2L)
				.name("최웅")
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
	@DisplayName("사용자가 없는 경우 - 계좌 생성 실패")
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
	@DisplayName("계좌 수가 10인 경우 - 계좌 생성 실패")
	void max_Account_Count_10_Test() {
		//given
		AccountUser choi = AccountUser.builder()
				.id(2L)
				.name("최웅")
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
	@DisplayName("계좌를 해지하는 경우 - 계좌 해지 성공")
	void deleteAccountTest() {
		//given
		AccountUser choi = AccountUser.builder()
				.id(2L)
				.name("최웅")
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
	@DisplayName("사용자가 없는 경우 - 계좌 해지 실패")
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
	@DisplayName("계좌가 없는 경우 - 계좌 해지 실패")
	void deleteAccountNotFoundTest() {
		//given
		AccountUser choi = AccountUser.builder()
				.id(2L)
				.name("최웅")
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
	@DisplayName("사용자와 계좌 소유주가 일치하지 않는 경우 - 계좌 해지 실패")
	void deleteAccountNotUserUnMatchTest() {
		//given
		AccountUser choi = AccountUser.builder()
				.id(1L)
				.name("최웅")
				.build();
		AccountUser guk = AccountUser.builder()
				.id(2L)
				.name("국연수")
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
	@DisplayName("계좌가 이미 해지인 경우 - 계좌 해지 실패")
	void deleteAccountAlreadyUnregisteredTest() {
		//given
		AccountUser choi = AccountUser.builder()
				.id(2L)
				.name("최웅")
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
	@DisplayName("잔액이 있는 경우 - 계좌 해지 실패")
	void deleteAccountBalanceNotZeroTest() {
		//given
		AccountUser choi = AccountUser.builder()
				.id(2L)
				.name("최웅")
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
	
	@Test
	@DisplayName("계좌를 확인하는 경우 - 계좌 확인 성공")
	void getAccountByUserIdTest() {
		//given
		AccountUser choi = AccountUser.builder()
				.id(2L)
				.name("최웅")
				.build();
		List<Account> accounts = Arrays.asList(
					Account.builder()
					.accountUser(choi)
					.accountNumber("1000000001")
					.balance(1000L)
					.build(),
					Account.builder()
					.accountUser(choi)
					.accountNumber("1000000002")
					.balance(2000L)
					.build(),
					Account.builder()
					.accountUser(choi)
					.accountNumber("1000000003")
					.balance(3000L)
					.build(),
					Account.builder()
					.accountUser(choi)
					.accountNumber("1000000004")
					.balance(4000L)
					.build()
		);
		
		given(accountUserRepository.findById(anyLong()))
				.willReturn(Optional.of(choi));
		given(accountRepository.findByAccountUser(any()))
				.willReturn(accounts);
		
		//when
		List<AccountDto> accountDtos = accountService.getAccountByUserId(1L);
		
		//then
		assertEquals(4, accountDtos.size());
		assertEquals("1000000001", accountDtos.get(0).getAccountNumber());
		assertEquals(1000L, accountDtos.get(0).getBalance());
		assertEquals("1000000002", accountDtos.get(1).getAccountNumber());
		assertEquals(2000L, accountDtos.get(1).getBalance());
		assertEquals("1000000003", accountDtos.get(2).getAccountNumber());
		assertEquals(3000L, accountDtos.get(2).getBalance());
		assertEquals("1000000004", accountDtos.get(3).getAccountNumber());
		assertEquals(4000L, accountDtos.get(3).getBalance());
	}
	
	@Test
	@DisplayName("사용자가 없는 경우 - 계좌 확인 실패")
	void notUserGetAccountByUserIdTest() {
		//given
		given(accountUserRepository.findById(anyLong()))
				.willReturn(Optional.empty());
		
		//when
		AccountException accountException = assertThrows(AccountException.class,
				() -> accountService.getAccountByUserId(1L));
		
		//then
		assertEquals(ErrorCode.USER_NOT_FOUND, accountException.getErrorCode());
	}
}
