package com.example.reservation.domain.security.details;

import com.example.reservation.domain.account.dto.AccountDto;
import com.example.reservation.domain.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        AccountDto accountDto = accountRepository.findByloginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("Can't find by loginId"));
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(accountDto.getAccountRole().toString()));
        return new AccountContext(accountDto, authorities);
    }
}
