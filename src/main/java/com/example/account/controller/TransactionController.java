package com.example.account.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.account.dto.UseBalance;
import com.example.account.exception.AccountException;
import com.example.account.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TransactionController {
	private final TransactionService transactionService;
	
	@PostMapping("/transaction/use")
	public UseBalance.Response useBalance(
			@RequestBody @Valid UseBalance.Request request
	) {
		try {
			return UseBalance.Response.from(transactionService.useBalance(
					request.getUsreId(), request.getAccountNumber(), request.getAmount()));
		} catch (AccountException e) {
			transactionService.saveFailedUseTransaction(
					request.getAccountNumber(), request.getAmount());
			throw e;
		}
	  }
}
