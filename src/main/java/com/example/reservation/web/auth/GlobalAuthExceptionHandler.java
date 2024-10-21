package com.example.reservation.web.auth;

import com.example.reservation.domain.exception.LoginIdExistsException;
import com.example.reservation.domain.security.exception.MissingHeaderException;
import com.example.reservation.domain.security.exception.UnSupportedHeaderException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalAuthExceptionHandler {

    @ExceptionHandler(LoginIdExistsException.class)
    public ResponseEntity<Map<String, Object>> handleLoginIdExistsException(LoginIdExistsException ex){
        //DB에 해당 ID가 있음
        //400
        Map<String, Object> response = new HashMap<>();
        response.put("messsage", ex.getMessage());
        return new ResponseEntity<>(response ,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingHeaderException.class)
    public ResponseEntity<Map<String, Object>> handleMissingHeaderException(MissingHeaderException ex){
        //필요한 헤더가 요청에 포함 안되어있으면 에러 보내주자.
        //401
        Map<String, Object> response = new HashMap<>();
        response.put("messsage", ex.getMessage());
        return new ResponseEntity<>(response ,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnSupportedHeaderException.class)
    public ResponseEntity<Map<String, Object>> handleUnSupportedHeaderException(UnSupportedHeaderException ex){
        //요청 헤더 값이 다르면
        //401
        Map<String, Object> response = new HashMap<>();
        response.put("messsage", ex.getMessage());
        return new ResponseEntity<>(response ,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationPasswordException(BadCredentialsException ex){
        //비밀번호가 다를때 처리
        //401
        Map<String, Object> response = new HashMap<>();
        response.put("messsage", ex.getMessage());
        return new ResponseEntity<>(response ,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUsernameNotFoundException(UsernameNotFoundException ex){
        //해당하는 loginId 없을때
        //401
        Map<String, Object> response = new HashMap<>();
        response.put("messsage", ex.getMessage());
        return new ResponseEntity<>(response ,HttpStatus.UNAUTHORIZED);
    }


}

