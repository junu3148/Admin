package com.lumen.www.jwt;

import io.jsonwebtoken.Claims;
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

    /**
     * 주어진 AdminUser 객체에 기반하여 JWT 토큰을 생성합니다.
     *
     * 이 메서드는 토큰의 헤더(header)와 페이로드(payload)를 설정하고,
     * 주어진 서명 알고리즘을 사용하여 토큰에 서명합니다.
     * 생성된 토큰은 AdminUser의 식별자, 역할 및 이름을 포함합니다.
     *
     * @param adminUser 토큰에 포함할 관리자 사용자 정보.
     * @return 생성된 JWT 토큰 문자열.
     */
    public String generateToken(AdminUser adminUser) {

        // Header 부분 설정: 토큰의 유형과 서명 알고리즘을 정의합니다.
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", TOKEN_TYPE);       // 토큰의 유형 (예: JWT)
        headers.put("alg", SIGNATURE_ALGORITHM); // 사용할 서명 알고리즘 (예: HS256)

        // Payload 부분 설정: 관리자 사용자의 ID, 역할, 이름을 포함합니다.
        Map<String, Object> payloads = new HashMap<>();
        payloads.put(CLAIM_ADMIN_USER_ID, adminUser.getAdminId());
        payloads.put(CLAIM_IS_ADMIN, adminUser.getRole());
        payloads.put(CLAIM_USER_NAME, adminUser.getAdminName());

        long now = System.currentTimeMillis();
        // 토큰 Builder를 사용하여 토큰을 생성합니다.
        return Jwts.builder()
                .setHeader(headers) // Headers 설정
                .setClaims(payloads) // Claims 설정
                .setSubject("AdminUser") // 토큰의 용도를 지정 (예: "AdminUser")
                .setExpiration(new Date(now + ACCESS_TOKEN_EXPIRE_COUNT)) // 토큰 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS256) // HS256 알고리즘과 키를 사용하여 서명
                .compact(); // 토큰을 문자열 형태로 압축하여 반환
    }


    /**
     * JWT 토큰에서 관리자 사용자 정보를 추출하여 AdminUser 객체로 반환합니다.
     *
     * @param token JWT 토큰 문자열.
     * @return 추출된 관리자 사용자 정보가 담긴 AdminUser 객체.
     */
    public AdminUser getAdminUserInfoFromToken(String token) {

        // AdminUser 객체를 생성합니다.
        AdminUser adminUser = new AdminUser();

        // 토큰을 파싱하여 Claims 객체를 얻습니다.
        Claims claims = parseToken(token);

        // Claims에서 관리자 ID, 역할, 이름을 추출합니다.
        String adminId = claims.get(CLAIM_ADMIN_USER_ID, String.class);
        String role = claims.get(CLAIM_IS_ADMIN, String.class);
        String adminName = claims.get(CLAIM_USER_NAME, String.class);

        // 추출한 정보를 AdminUser 객체에 설정합니다.
        adminUser.setAdminId(adminId);
        adminUser.setRole(Integer.parseInt(role)); // 문자열 형태의 역할을 정수로 변환합니다.
        adminUser.setAdminName(adminName);

        // 완성된 AdminUser 객체를 반환합니다.
        return adminUser;
    }



    /**
     * 주어진 JWT 토큰을 파싱하여 Claims 객체를 반환합니다.
     * 이 메서드는 JWT 토큰의 유효성을 검증하고, 토큰 내부에 저장된 클레임(claim)들을 추출합니다.
     *
     * @param token 파싱할 JWT 토큰 문자열.
     * @return 토큰에서 추출된 Claims 객체.
     * @throws io.jsonwebtoken.JwtException 토큰이 유효하지 않거나 파싱 중 문제가 발생한 경우.
     */
    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key) // JWT 토큰을 검증하기 위한 서명 키 설정
                .build()           // JwtParserBuilder 인스턴스를 JwtParser로 빌드
                .parseClaimsJws(token) // 토큰을 파싱하여 Claims JWS 객체를 얻음
                .getBody();            // Claims JWS 객체에서 Claims(클레임 세트)를 추출
    }

}