package com.example.reservation.domain.account.repository;

import com.example.reservation.domain.account.dto.AccountDto;
import com.example.reservation.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT new com.example.reservation.domain.account.dto.AccountDto" +
            "(a.id, a.loginId, a.password, a.name, a.phoneNumber, a.email, a.accountRole)" +
            "FROM Account a WHERE a.loginId = :loginId")
    Optional<AccountDto> findByloginId(String loginId);
}
