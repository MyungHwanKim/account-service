package com.example.account.exception;

import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.account.dto.ErrorResponse;
import com.example.account.type.ErrorCode;

@RestControllerAdvice
public class ExceptionController {

	@ExceptionHandler(AccountException.class)
	public ErrorResponse accountValidException(AccountException e) {
		return new ErrorResponse(e.getErrorCode(), e.getErrorDescription(), LocalDateTime.now());
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ErrorResponse MethodArgumentNotValidException(MethodArgumentNotValidException e) {
		return new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR, 
				ErrorCode.INTERNAL_SERVER_ERROR.getDescription(), LocalDateTime.now());
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ErrorResponse DataIntegrityViolationValidException(DataIntegrityViolationException e) {
		return new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR, 
				ErrorCode.INTERNAL_SERVER_ERROR.getDescription(), LocalDateTime.now());
	}
	
	@ExceptionHandler(Exception.class)
	public ErrorResponse ValidException(Exception e) {
		return new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR, 
				ErrorCode.INTERNAL_SERVER_ERROR.getDescription(), LocalDateTime.now());
	}
}
