package com.example.springsecuritytest.exception;

import org.springframework.security.core.AuthenticationException;

public class ValidateCodeException extends AuthenticationException {

    private static final long serialVersionUID = -2866301886317422588L;

    public ValidateCodeException(String message) {
        super(message);
    }
}