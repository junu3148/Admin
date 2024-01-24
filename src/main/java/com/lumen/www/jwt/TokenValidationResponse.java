package com.lumen.www.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenValidationResponse {
    private boolean valid;
    private String errorMessage;
    private TokenType errorType;

    public TokenValidationResponse() {
        // 기본 생성자 추가
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public TokenType getErrorType() {
        return errorType;
    }

    public void setErrorType(TokenType errorType) {
        this.errorType = errorType;
    }

    public TokenValidationResponse validateToken(String token, String key) {
        TokenValidationResponse response = new TokenValidationResponse();

        try {
            JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build();
            parser.parseClaimsJws(token);

            response.setValid(true);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            response.setValid(false);
            response.setErrorMessage("Token expired");
            response.setErrorType(TokenType.EXPIRED);
        } catch (MalformedJwtException | SecurityException e) {
            log.info("Invalid JWT Token", e);
            response.setValid(false);
            response.setErrorMessage("Invalid token");
            response.setErrorType(TokenType.INVALID);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
            response.setValid(false);
            response.setErrorMessage("Unsupported token");
            response.setErrorType(TokenType.UNSUPPORTED);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
            response.setValid(false);
            response.setErrorMessage("Empty claims in token");
            response.setErrorType(TokenType.EMPTY_CLAIMS);
        }

        return response;
    }
}

