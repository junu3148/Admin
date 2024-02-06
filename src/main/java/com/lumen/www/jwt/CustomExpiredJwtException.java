package com.lumen.www.jwt;

import io.jsonwebtoken.JwtException;

public class CustomExpiredJwtException extends JwtException {
    public CustomExpiredJwtException(String message) {
        super(message);
    }

    public CustomExpiredJwtException(String message, Throwable cause) {
        super(message, cause);
    }
}