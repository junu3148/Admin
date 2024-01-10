package com.lumen.www.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.lumen.www.dto.AdminUser;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class JwtTokenProvider {

    private final Key key;
    public final long ACCESS_TOKEN_EXPIRE_COUNT = 30 * 60 * 1000L; // 30분

    private static final String TOKEN_TYPE = "JWT";
    private static final String SIGNATURE_ALGORITHM = "HS256";
    private static final String CLAIM_ADMIN_USER_ID = "adminUserId";
    private static final String CLAIM_IS_ADMIN = "isAdmin";
    private static final String CLAIM_USER_NAME = "userName";

    // application.yml에서 secret 값 가져와서 key에 저장
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalArgumentException("Secret key cannot be null or empty.");
        }
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(AdminUser adminUser) {

        // Header 부분 설정
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", TOKEN_TYPE);       // 토큰의 유형
        headers.put("alg", SIGNATURE_ALGORITHM); // 서명 알고리즘

        // Payload 부분 설정
        Map<String, Object> payloads = new HashMap<>();
        payloads.put(CLAIM_ADMIN_USER_ID, adminUser.getAdminId());
        payloads.put(CLAIM_IS_ADMIN, adminUser.getRole());
        payloads.put(CLAIM_USER_NAME, adminUser.getAdminName());

        long now = System.currentTimeMillis();
        // 토큰 Builder
        return Jwts.builder()
                .setHeader(headers) // Headers 설정
                .setClaims(payloads) // Claims 설정
                .setSubject("AdminUser") // 토큰 용도(제목)
                .setExpiration(new Date(now + ACCESS_TOKEN_EXPIRE_COUNT)) // 토큰 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS256) // HS256과 Key로 Sign
                .compact(); // 토큰 생성
    }

}