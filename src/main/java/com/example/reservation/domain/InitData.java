package com.example.reservation.domain;

import com.example.reservation.domain.account.entity.Account;
import com.example.reservation.domain.account.entity.AccountRole;
import com.example.reservation.domain.account.repository.AccountRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitData {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initdata(){
        Account account = Account.createAccount("user123",
                passwordEncoder.encode("aud435t1n@"),
                "test",
                "test@gmail.com",
                "010-0000-0000",
                AccountRole.USER);
        accountRepository.save(account);
    }

}
