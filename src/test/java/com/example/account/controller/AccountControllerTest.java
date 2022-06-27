package com.example.account.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.account.dto.AccountDto;
import com.example.account.dto.CreateAccount;
import com.example.account.dto.DeleteAccount;
import com.example.account.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AccountController.class)
class AccountControllerTest {
	@MockBean
	private AccountService accountService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	void createAccountTest() throws Exception {
		//given
		given(accountService.createAccount(anyLong(), anyLong()))
				.willReturn(AccountDto.builder()
						.userId(1L)
						.accountNumber("1111111111")
						.registeredAt(LocalDateTime.now())
						.unregisteredAt(LocalDateTime.now())
						.build());
		//when
		//then
		mockMvc.perform(post("/account")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(
						new CreateAccount.Request(1L, 100L)
				)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.userId").value(1L))
				.andExpect(jsonPath("$.accountNumber").value("1111111111"))
				.andDo(print());
	}
	
	@Test
	void deleteAccountTest() throws Exception {
		//given
		given(accountService.deleteAccount(anyLong(), anyString()))
				.willReturn(AccountDto.builder()
						.userId(1L)
						.accountNumber("1111111111")
						.registeredAt(LocalDateTime.now())
						.unregisteredAt(LocalDateTime.now())
						.build());
		//when
		//then
		mockMvc.perform(delete("/account")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(
						new DeleteAccount.Request(1L, "1000000000")
				)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.userId").value(1L))
				.andExpect(jsonPath("$.accountNumber").value("1111111111"))
				.andDo(print());
	}
	
	@Test
	void getAccountByUserId() throws Exception {
		//given
		List<AccountDto> acountDtos = Arrays.asList(
						AccountDto.builder()
						.accountNumber("1000000001")
						.balance(1000L).build(),
						AccountDto.builder()
						.accountNumber("1000000002")
						.balance(2000L).build(),
						AccountDto.builder()
						.accountNumber("1000000003")
						.balance(3000L).build(),
						AccountDto.builder()
						.accountNumber("1000000004")
						.balance(4000L).build()
		);
		
		given(accountService.getAccountByUserId(anyLong()))
				.willReturn(acountDtos);
		
		//when
		//then
		mockMvc.perform(get("/account?user_id=1"))
				.andDo(print())
				.andExpect(jsonPath("$[0].accountNumber").value("1000000001"))
				.andExpect(jsonPath("$[0].balance").value(1000L))
				.andExpect(jsonPath("$[1].accountNumber").value("1000000002"))
				.andExpect(jsonPath("$[1].balance").value(2000L))
				.andExpect(jsonPath("$[2].accountNumber").value("1000000003"))
				.andExpect(jsonPath("$[2].balance").value(3000L))
				.andExpect(jsonPath("$[3].accountNumber").value("1000000004"))
				.andExpect(jsonPath("$[3].balance").value(4000L));
	}
	

}
