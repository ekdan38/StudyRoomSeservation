package com.example.reservation.domain.security.handler;

import com.example.reservation.domain.security.exception.MissingHeaderException;
import com.example.reservation.domain.security.exception.UnSupportedHeaderException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Slf4j
public class RestAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        //인증에 실패했기때문에 401예외 내주자.
        //advice controller에서 모아서 처리해주자.
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("errors", "Authentication failed");

        if(exception instanceof UsernameNotFoundException){
            log.info("UsernameNotFoundException");
            responseBody.put("message", exception.getMessage());
        }
        if(exception instanceof MissingHeaderException){
            log.info("MissingHeaderException");
            responseBody.put("message", exception.getMessage());
        }
        if(exception instanceof UnSupportedHeaderException){
            log.info("UnSupportedHeaderException");
            responseBody.put("message", exception.getMessage());
        }
        if(exception instanceof BadCredentialsException){
            log.info("BadCredentialsException");
            responseBody.put("message", exception.getMessage());
        }
        else {
            log.info("{}", exception.getClass().getSimpleName());
            responseBody.put("message", exception.getMessage());
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }
}
