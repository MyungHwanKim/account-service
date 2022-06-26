package com.example.account.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.account.domain.Account;
import com.example.account.domain.AccountUser;
import com.example.account.domain.Transaction;
import com.example.account.dto.TransactionDto;
import com.example.account.exception.AccountException;
import com.example.account.repository.AccountRepository;
import com.example.account.repository.AccountUserRepository;
import com.example.account.repository.TransactionRepository;
import com.example.account.type.AccountStatus;
import com.example.account.type.ErrorCode;
import com.example.account.type.TransactionResultType;
import com.example.account.type.TransactionType;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

	@Mock
	private TransactionRepository transactionRepository;
	
	@Mock
	private AccountUserRepository accountUserRepository;
	
	@Mock
	private AccountRepository accountRepository;
	
	@InjectMocks
	private TransactionService transactionService;
	
	@Test
	@DisplayName("���� �ܾ��� ����ϴ� ��� - ���� �ܾ� ����")
	void useBalanceTest() {
		//given
		AccountUser choi = AccountUser.builder()
				.id(2L)
				.name("�ֿ�")
				.build();
		
		given(accountUserRepository.findById(anyLong()))
			.willReturn(Optional.of(choi));
		
		Account account = Account.builder()
				.accountUser(choi)
				.accountNumber("1000000010")
				.accountStatus(AccountStatus.IN_USE)
				.balance(10000L)
				.build();
		given(accountRepository.findByAccountNumber(anyString()))
			.willReturn(Optional.of(account));
		given(transactionRepository.save(any()))
			.willReturn(Transaction.builder()
					.transactionType(TransactionType.USE)
					.tranactionResultType(TransactionResultType.SUCCESS)
					.account(account)
					.amount(1000L)
					.balanceSnapshot(9000L)
					.transactionId("transactionId")
					.build());
		//when
		TransactionDto transactionDto = transactionService.useBalance(1L, "1000000001", 1000L);
		
		//then
		assertEquals(TransactionType.USE, transactionDto.getTransactionType());
		assertEquals(TransactionResultType.SUCCESS, transactionDto.getTransactionResultType());
		assertEquals(9000L, transactionDto.getBalanceSnapshot());
		assertEquals(1000L, transactionDto.getAmount());
	}
	
	@Test
	@DisplayName("����ڰ� ���� ��� - �ܾ� ��� ����")
	void userNotFoundTest() {
		//given
		given(accountUserRepository.findById(anyLong()))
				.willReturn(Optional.empty());
		
		//when
		AccountException accountException = assertThrows(AccountException.class,
				() -> transactionService.useBalance(1L, "1000000010", 1000L));
		
		//then
		assertEquals(ErrorCode.USER_NOT_FOUND, accountException.getErrorCode());
	}
	
	@Test
	@DisplayName("���°� ���� ��� - �ܾ� ��� ����")
	void transactionAccountNotFoundTest() {
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
				() -> transactionService.useBalance(1L, "1000000010", 1000L));
		
		//then
		assertEquals(ErrorCode.ACCOUNT_NOT_FOUND, accountException.getErrorCode());
	}

	@Test
	@DisplayName("����ڿ� ���� �����ְ� ��ġ���� �ʴ� ��� - �ܾ� ��� ����")
	void transactionAccountNotUserUnMatchTest() {
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
				() -> transactionService.useBalance(1L, "1000000010", 1000L));
		
		//then
		assertEquals(ErrorCode.USER_ACCOUNT_UNMATCH, accountException.getErrorCode());
	}
	
	@Test
	@DisplayName("���°� �̹� ������ ��� - �ܾ� ��� ����")
	void transactionAccountAlreadyUnregisteredTest() {
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
				() -> transactionService.useBalance(1L, "1000000010", 1000L));
		
		//then
		assertEquals(ErrorCode.ACCOUNT_ALREADY_UNREGISTERED, accountException.getErrorCode());
	}
	
	@Test
	@DisplayName("�ŷ��ݾ��� �ܾ׺��� ū ��� - �ܾ� ��� ����")
	void exceedThanBalanceTest() {
		//given
		AccountUser choi = AccountUser.builder()
				.id(2L)
				.name("�ֿ�")
				.build();
		
		given(accountUserRepository.findById(anyLong()))
			.willReturn(Optional.of(choi));
		
		Account account = Account.builder()
				.accountUser(choi)
				.accountNumber("1000000010")
				.accountStatus(AccountStatus.IN_USE)
				.balance(999L)
				.build();
		given(accountRepository.findByAccountNumber(anyString()))
			.willReturn(Optional.of(account));

		
		//when
		AccountException accountException = assertThrows(AccountException.class,
				() -> transactionService.useBalance(1L, "1000000010", 1000L));
		
		//then
		assertEquals(ErrorCode.EXCEED_THAN_BALANCE, accountException.getErrorCode());
	}
	
	@Test
	@DisplayName("���� �ŷ��� ���� ������ ���")
	void saveFailedUseTransaction() {
		//given
		AccountUser choi = AccountUser.builder()
				.id(2L)
				.name("�ֿ�")
				.build();
		
		Account account = Account.builder()
				.accountUser(choi)
				.accountNumber("1000000010")
				.accountStatus(AccountStatus.IN_USE)
				.balance(10000L)
				.build();
		
		given(accountRepository.findByAccountNumber(anyString()))
			.willReturn(Optional.of(account));
		given(transactionRepository.save(any()))
			.willReturn(Transaction.builder()
				.transactionType(TransactionType.USE)
				.tranactionResultType(TransactionResultType.SUCCESS)
				.account(account)
				.amount(1000L)
				.balanceSnapshot(9000L)
				.transactionId("transactionId")
				.build());
		
		ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
		
		//when
		transactionService.saveFailedUseTransaction("1000000010", 1000L);
		
		//then
		verify(transactionRepository, times(1)).save(captor.capture());
		assertEquals(1000L, captor.getValue().getAmount());
		assertEquals(10000L, captor.getValue().getBalanceSnapshot());
		assertEquals(TransactionResultType.FAIL, captor.getValue().getTranactionResultType());
	}
}
