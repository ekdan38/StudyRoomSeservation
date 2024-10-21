package com.example.reservation.domain.account.repository;

import com.example.reservation.domain.account.dto.AccountDto;
import com.example.reservation.domain.account.entity.Account;
import com.example.reservation.domain.account.entity.AccountRole;
import com.example.reservation.domain.mapper.AccountMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountRepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountMapper accountMapper;

    @Test
    @Transactional
    public void findByLoginIdSuccess(){
        //given
        AccountDto accountDto = new AccountDto(1L,
                "testId",
                "1234",
                "test",
                "010-0000-0000",
                "test@gmail.com",
                AccountRole.USER);

        Account account = accountMapper.DtoToEntity(accountDto);
        accountRepository.save(account);

        //when
        AccountDto foudAccountDto = accountRepository.findByloginId(accountDto.getLoginId())
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 loginId없음"));

        //then
        Assertions.assertThat(foudAccountDto.getLoginId()).isEqualTo(accountDto.getLoginId());

    }


    @Test
    @Transactional
    public void findByLoginIdSuccessFail(){
        //given
        AccountDto accountDto = new AccountDto(1L,
                "testId",
                "1234",
                "test",
                "010-0000-0000",
                "test@gmail.com",
                AccountRole.USER);

        Account account = accountMapper.DtoToEntity(accountDto);
        accountRepository.save(account);

        //when
        Optional<AccountDto> foundAccountDto = accountRepository.findByloginId("NologinId");

        //then
        Assertions.assertThat(foundAccountDto).isEmpty();
    }

}