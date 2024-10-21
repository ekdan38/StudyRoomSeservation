package com.example.reservation.domain.security.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 인증되지 않은 사용자가 보호된 자원에 접근했을 때 URL 저장
        String attemptedUri = request.getRequestURI();
        log.info("Unauthenticated access attempt to: {}", attemptedUri);

        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("errors", "Authentication required");
        responseBody.put("message", authException.getMessage());
        responseBody.put("redirectUrl", attemptedUri);


        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
        //401응답을 내기에 savedRequest를 사용할 수 없다고 한다.

        //여기서는 redirectUrl을 전달만 해주자.
        //restapi는 redirect 를 해줄 필요가 없다.

    }
}
