package com.example.reservation.domain.security.exception;

import org.springframework.security.authentication.AuthenticationServiceException;

public class UnSupportedHeaderException extends AuthenticationServiceException {
    public UnSupportedHeaderException(String msg) {
        super(msg);
    }
}
