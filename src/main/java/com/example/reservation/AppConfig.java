package com.example.reservation;

import com.example.reservation.domain.mapper.AccountMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper mapper(){
        return new ModelMapper();
    }

    @Bean
    public AccountMapper accountMapper(){
        return new AccountMapper();
    }
}
