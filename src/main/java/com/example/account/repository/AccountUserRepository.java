package com.example.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.account.domain.AccountUser;

@Repository
public interface AccountUserRepository extends JpaRepository<AccountUser, Long>{

}
