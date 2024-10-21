package com.example.reservation.domain.account.sevice;

import com.example.reservation.domain.account.dto.AccountDto;
import com.example.reservation.domain.account.entity.Account;
import com.example.reservation.domain.account.entity.AccountRole;
import com.example.reservation.domain.account.repository.AccountRepository;
import com.example.reservation.domain.exception.LoginIdExistsException;
import com.example.reservation.domain.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Account accountSignup(AccountDto accountDto) {

        if(accountRepository.findByloginId(accountDto.getLoginId()).isPresent()){
            throw new LoginIdExistsException("LoginId is already exists");
        }
        //예외처리 해주자.


        accountDto.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        Account account = Account.createAccount(
                accountDto.getLoginId(),
                accountDto.getPassword(),
                accountDto.getName(),
                accountDto.getPhoneNumber(),
                accountDto.getEmail(),
                accountDto.getAccountRole()
        );
        return accountRepository.save(account);
    }


}
