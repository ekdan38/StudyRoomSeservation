package com.example.reservation.domain.account.dto;

import com.example.reservation.domain.account.entity.AccountRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private Long id;
    private String loginId;
    private String password;
    private String name;
    private String phoneNumber;
    private String email;
    private AccountRole accountRole; //role은 계층 구조 줄거여서 한개의 역할 부여.

    public AccountDto(String loginId, String password, String name, String phoneNumber, String email, AccountRole accountRole) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.accountRole = accountRole;
    }
}
