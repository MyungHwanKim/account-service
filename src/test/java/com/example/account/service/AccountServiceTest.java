package com.example.account.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
		AccountUser user = AccountUser.builder()
				.id(2L)
				.name("최웅")
				.build();
		
		given(accountUserRepository.findById(anyLong()))
				.willReturn(Optional.of(user));
		given(accountRepository.findFirstByOrderByIdDesc())
				.willReturn(Optional.of(Account.builder()
						.accountNumber("1000000010")
						.balance(1000L).build()));
		given(accountRepository.save(any()))
				.willReturn(Account.builder()
						.accountUser(user)
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
		AccountUser user = AccountUser.builder()
				.id(2L)
				.name("최웅")
				.build();
		
		given(accountUserRepository.findById(anyLong()))
				.willReturn(Optional.of(user));
		given(accountRepository.findFirstByOrderByIdDesc())
				.willReturn(Optional.empty());
		given(accountRepository.save(any()))
				.willReturn(Account.builder()
						.accountUser(user)
						.accountNumber("1000000012")
						.build());
		
		//when
		AccountDto accountDto = accountService.createAccount(1L, 1000L);
		
		//then
		assertEquals(2L, accountDto.getUserId());
	}
	
	@Test
	@DisplayName("사용자가 없는 경우 - 계좌 생성 실패")
	void UserNotFoundTest() {
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
	void MAX_ACCOUNT_COUNT_10_Test() {
		//given
		AccountUser user = AccountUser.builder()
				.id(2L)
				.name("최웅")
				.build();
		given(accountUserRepository.findById(anyLong()))
		.willReturn(Optional.of(user));
		given(accountRepository.countByAccountUser(any()))
				.willReturn(10);
		
		//when
		AccountException accountException = assertThrows(AccountException.class,
				() -> accountService.createAccount(1L, 1000L));
		
		//then
		assertEquals(ErrorCode.MAX_ACCOUNT_COUNT_10, accountException.getErrorCode());
	}
}
