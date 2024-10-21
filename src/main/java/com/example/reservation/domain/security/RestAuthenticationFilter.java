package com.example.reservation.domain.security;

import com.example.reservation.domain.security.dto.AccountLoginDto;
import com.example.reservation.domain.security.exception.MissingHeaderException;
import com.example.reservation.domain.security.exception.UnSupportedHeaderException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

public class RestAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RestAuthenticationFilter(HttpSecurity http) {
        super(new AntPathRequestMatcher("/login", "POST"));
        // ("/login")으로 Post요청만 처리
        setSecurityContextRepository(getSecurityContextRepository(http));
    }
    //세션 인증 유지, threadLocal에 설정
    private SecurityContextRepository getSecurityContextRepository(HttpSecurity http) {
        SecurityContextRepository securityContextRepository = http.getSharedObject(SecurityContextRepository.class);
        if(securityContextRepository == null){
            securityContextRepository = new DelegatingSecurityContextRepository(
                    new RequestAttributeSecurityContextRepository(), new HttpSessionSecurityContextRepository()
            );
        }
        return securityContextRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        //로그인은 비동기 통신 처리 (Header에 X-Request-With:XMLHttpRequest)
        String ajaxHeader = "X-Requested-With";
        String ajaxHeaderValue = request.getHeader(ajaxHeader);

        if(ajaxHeaderValue == null){
            throw new MissingHeaderException("Missing X-Requested-With Header");
        }

        if(!ajaxHeaderValue.equals("XMLHttpRequest")){
            throw new UnSupportedHeaderException("This Header is not Supported");
        }

        AccountLoginDto accountLoginDto = objectMapper.readValue(request.getReader(), AccountLoginDto.class);
        RestAuthenticationToken restAuthenticationToken = new RestAuthenticationToken(accountLoginDto.getLoginId(), accountLoginDto.getPassword());
        return getAuthenticationManager().authenticate(restAuthenticationToken);
    }
}
