package com.example.account.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.account.dto.CancelBalance;
import com.example.account.dto.TransactionDto;
import com.example.account.dto.UseBalance;
import com.example.account.service.RedisTestService;
import com.example.account.service.TransactionService;
import com.example.account.type.TransactionResultType;
import com.example.account.type.TransactionType;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {
	@MockBean
	private TransactionService transactionService;
	
	@MockBean
	private RedisTestService redisTestService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	
	@Test
	void useBalanceTest() throws Exception {
		//given
		given(transactionService.useBalance(anyLong(), anyString(), anyLong()))
				.willReturn(TransactionDto.builder()
						.accountNumber("1000000010")
						.transactionResultType(TransactionResultType.SUCCESS)
						.amount(10000L)
						.transactionId("transactionIdUsingFor1000000010a")
						.transactedAt(LocalDateTime.now())
						.build());
		
		//when
		//then
		mockMvc.perform(post("/transaction/use")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(
						new UseBalance.Request(1L, "1000000010", 1000L)))
				)
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.accountNumber").value("1000000010"))
		.andExpect(jsonPath("$.transactionResult").value("SUCCESS"))
		.andExpect(jsonPath("$.amount").value(10000L))
		.andExpect(jsonPath("$.transactionId").value("transactionIdUsingFor1000000010a"));
	}

	@Test
	void cancelBalanceTest() throws Exception {
		//given
		given(transactionService.cancelBalance(anyString(), anyString(), anyLong()))
				.willReturn(TransactionDto.builder()
						.accountNumber("1000000010")
						.transactionResultType(TransactionResultType.SUCCESS)
						.amount(10000L)
						.transactionId("transactionIdCancelfor1000000010")
						.transactedAt(LocalDateTime.now())
						.build());
		
		//when
		//then
		mockMvc.perform(post("/transaction/cancel")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(
						new CancelBalance.Request(
								"transactionIdCancelfor1000000010"
								, "1000000010", 1000L)))
				)
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.accountNumber").value("1000000010"))
		.andExpect(jsonPath("$.transactionResultType").value("SUCCESS"))
		.andExpect(jsonPath("$.amount").value(10000L))
		.andExpect(jsonPath("$.transactionId").value("transactionIdCancelfor1000000010"));
	}
	
	@Test
	void confirmTransactionTest() throws Exception {
		//given
		given(transactionService.ConfirmTransaction(anyString()))
				.willReturn(TransactionDto.builder()
						.accountNumber("1000000010")
						.transactionType(TransactionType.USE)
						.transactionResultType(TransactionResultType.SUCCESS)
						.amount(10000L)
						.transactionId("transactionIdCancelfor1000000010")
						.transactedAt(LocalDateTime.now())
						.build());
		
		//when
		//then
		mockMvc.perform(get("/transaction/transactionIdCancelfor1000000010"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.accountNumber").value("1000000010"))
			.andExpect(jsonPath("$.transactionType").value("USE"))
			.andExpect(jsonPath("$.transactionResultType").value("SUCCESS"))
			.andExpect(jsonPath("$.amount").value(10000L))
			.andExpect(jsonPath("$.transactionId").value("transactionIdCancelfor1000000010"));
	}
}
