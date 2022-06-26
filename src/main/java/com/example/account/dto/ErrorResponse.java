package com.example.account.dto;

import java.time.LocalDateTime;

import com.example.account.type.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
	private ErrorCode errorCode;
	private String errorDescription;
	private LocalDateTime currentedAt;
}
