package com.example.reservation.domain.mapper;

import com.example.reservation.domain.account.dto.AccountDto;
import com.example.reservation.domain.account.entity.Account;
import com.example.reservation.domain.account.entity.AccountRole;
import com.example.reservation.web.auth.dto.AccountSignupRequestDto;

public class AccountMapper {

    public AccountDto EntityToDto(Account account) {
        return new AccountDto(
                account.getId(),
                account.getLoginId(),
                account.getPassword(),
                account.getName(),
                account.getPhoneNumber(),
                account.getEmail(),
                account.getAccountRole()
        );
    }

    public AccountDto requestToDto(AccountSignupRequestDto requestDto){
        return new AccountDto(
                requestDto.getLoginId(),
                requestDto.getPassword(),
                requestDto.getName(),
                requestDto.getPhoneNumber(),
                requestDto.getEmail(),
                getAccountRole(requestDto.getAccountRole())
        );
    }

    public Account DtoToEntity(AccountDto accountDto){
        return Account.createAccount(
                accountDto.getLoginId(),
                accountDto.getPassword(),
                accountDto.getName(),
                accountDto.getPhoneNumber(),
                accountDto.getEmail(),
                accountDto.getAccountRole()
        );
    }


    private AccountRole getAccountRole(String accountRole){
        if(accountRole.equals("MANAGER")){
            return AccountRole.MANAGER;
        }
        else return AccountRole.USER;
    }
}
