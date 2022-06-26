package com.example.account.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.account.exception.AccountException;
import com.example.account.type.AccountStatus;
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
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="account")
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private AccountUser accountUser;
	private String accountNumber;
	
	@Enumerated(EnumType.STRING)
	private AccountStatus accountStatus;
	private Long balance;
	
	private LocalDateTime registeredAt;
	private LocalDateTime unregisteredAt;
	
	@CreatedDate
	private LocalDateTime createdAt;
	@LastModifiedDate
	private LocalDateTime updatedAt;
	
	public void useBalance(Long amount) {
		if (amount > balance) {
			throw new AccountException(ErrorCode.EXCEED_THAN_BALANCE);
		}
		balance -= amount;
	}
	
	public void cancelBalance(Long amount) {
		if (amount < 0) {
			throw new AccountException(ErrorCode.INVALID_REQUEST);
		}
		balance += amount;
	}
}
