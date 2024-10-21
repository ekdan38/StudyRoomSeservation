package com.example.reservation.domain.exception;

public class LoginIdExistsException extends IllegalArgumentException {
    public LoginIdExistsException(String message) {
        super(message);
    }
}
