package com.lumen.www.jwt;

import com.lumen.www.dto.AdminUser;
import com.lumen.www.dto.JwtToken;
import com.lumen.www.exception.InvalidTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

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

    // Member 정보를 가지고 AccessToken, RefreshToken을 생성하는 메서드
    public JwtToken generateToken(Authentication authentication) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + 86400000);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + 86400000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
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

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new InvalidTokenException("권한 정보가 없는 토큰입니다."); // 사용자 정의 예외
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
            throw new InvalidTokenException("Invalid JWT Token", e); // 사용자 정의 예외
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            // 만료된 토큰에 대한 처리, 필요에 따라
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
            throw new InvalidTokenException("Unsupported JWT Token", e); // 사용자 정의 예외
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
            throw new InvalidTokenException("JWT claims string is empty.", e); // 사용자 정의 예외
        }
        return false;
    }

    // accessToken
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}