package com.example.account.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.account.dto.CancelBalance;
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
			@RequestBody @Valid UseBalance.Request request)
	{
		try {
			return UseBalance.Response.from(transactionService.useBalance(
					request.getUserId(), request.getAccountNumber(), request.getAmount()));
		} catch (AccountException e) {
			transactionService.saveFailedUseTransaction(
					request.getAccountNumber(), request.getAmount());
			throw e;
		}
	}
	
	@PostMapping("/transaction/cancel")
	public CancelBalance.Response cancelBalance(
			@RequestBody @Valid CancelBalance.Request request)
	{
		try {
			return CancelBalance.Response.from(transactionService.cancelBalance(
					request.getTransactionId(), request.getAccountNumber(), request.getAmount()));
		} catch (AccountException e) {
			transactionService.saveFailedCancelTransaction(
					request.getAccountNumber(), request.getAmount());
			throw e;
		}
	}
}
