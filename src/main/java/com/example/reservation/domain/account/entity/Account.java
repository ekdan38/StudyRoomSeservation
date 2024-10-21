package com.example.reservation.domain.account.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String loginId;
    private String password;
    private String name;
    private String phoneNumber;
    private String email;
    private AccountRole accountRole; //role은 계층 구조 줄거여서 한개의 역할 부여.

    private Account(String loginId, String password, String name, String phoneNumber, String email, AccountRole accountRole) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.accountRole = accountRole;
    }

    public static Account createAccount(String loginId, String password, String name, String phoneNumber, String email, AccountRole accountRole){
        return new Account(loginId, password, name, phoneNumber, email, accountRole);
    }
}
