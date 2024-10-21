package com.example.reservation.domain.security.exception;

import org.springframework.security.authentication.AuthenticationServiceException;

public class MissingHeaderException extends AuthenticationServiceException {
    public MissingHeaderException(String msg) {
        super(msg);
    }
}
