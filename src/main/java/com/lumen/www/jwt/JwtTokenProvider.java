package com.lumen.www.jwt;

import com.lumen.www.dao.TokenRepository;
import com.lumen.www.dto.AdminUser;
import com.lumen.www.dto.JwtToken;
import com.lumen.www.exception.InvalidTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final TokenRepository tokenRepository;
    public final long ACCESS_TOKEN_EXPIRE_COUNT = 60 * 60 * 1000L; // 1시간
    public final long REFRESH_TOKEN_EXPIRE_COUNT = (8 * 60 * 60 * 1000); //8시간
    private static final String TOKEN_TYPE = "JWT";
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    private static final String CLAIM_ADMIN_USER_ID = "adminUserId";
    private static final String CLAIM_IS_ADMIN = "isAdmin";
    private static final String CLAIM_USER_NAME = "userName";


    /**
     * JWT (JSON Web Token) 토큰을 생성 및 검증하는 클래스입니다.
     * 이 클래스는 주어진 시크릿 키(secret key)를 사용하여 JWT 토큰을 생성하고 검증합니다.
     *
     * @param secretKey JWT 토큰을 서명하기 위한 비밀 키 문자열.
     * @throws IllegalArgumentException 시크릿 키가 null 또는 빈 문자열인 경우 발생하는 예외.
     */
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey, TokenRepository tokenRepository) {
        // 시크릿 키가 null 또는 빈 문자열인 경우 예외를 발생시킵니다.
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalArgumentException("Secret key cannot be null or empty.");
        }

        // Base64 디코딩을 사용하여 시크릿 키 문자열을 바이트 배열로 변환합니다.
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);

        // 변환된 바이트 배열을 사용하여 HmacSHA 키를 생성합니다.
        this.key = Keys.hmacShaKeyFor(keyBytes);
        // TokenRepository 인스턴스를 할당합니다.

        // TokenRepository 인스턴스를 할당합니다.
        this.tokenRepository = tokenRepository;
    }

    public JwtToken generateToken(Authentication authentication) {
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        // Access Token 유효시간: 30분 (30 * 60 * 1000)
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_COUNT);
        String accessToken = Jwts.builder()
                .setHeaderParam("typ", TOKEN_TYPE)
                .setSubject(authentication.getName())
                .claim("roles", roles)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SIGNATURE_ALGORITHM)
                .compact();

        // Refresh Token 유효시간: 4시간 (4 * 60 * 60 * 1000)
        Date refreshTokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRE_COUNT);
        String refreshToken = Jwts.builder()
                .setHeaderParam("typ", TOKEN_TYPE)
                .setExpiration(refreshTokenExpiresIn)
                .signWith(key, SIGNATURE_ALGORITHM)
                .compact();


        // 데이터베이스에 리플레시 토큰 저장
        tokenRepository.saveRefreshToken(authentication.getName(), refreshToken, refreshTokenExpiresIn);

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // Member 정보를 가지고 AccessToken, RefreshToken을 생성하는 메서드
 /*   public JwtToken generateToken(Authentication authentication) {
        // 권한 가져오기
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + 86400000);
        String accessToken = Jwts.builder()
                .setHeaderParam("typ", TOKEN_TYPE)
                .setSubject(authentication.getName())
                .claim("roles", roles)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SIGNATURE_ALGORITHM)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setHeaderParam("typ", TOKEN_TYPE)
                .setExpiration(new Date(now + 86400000))
                .signWith(key, SIGNATURE_ALGORITHM)
                .compact();


        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }*/

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

    /**
     * 주어진 JWT 액세스 토큰을 사용하여 인증(Authentication) 객체를 생성하여 반환합니다.
     * 이 메서드는 JWT 토큰에서 추출한 권한 정보를 사용하여 Spring Security의 Authentication 객체를 생성합니다.
     *
     * @param accessToken JWT 액세스 토큰 문자열.
     * @return 생성된 Authentication 객체.
     * @throws InvalidTokenException 토큰이 유효하지 않거나 권한 정보가 없는 경우 발생할 수 있는 예외.
     */
    public Authentication getAuthentication(String accessToken) {
        // JWT 토큰을 해석하여 클레임(Claims) 객체를 추출합니다.
        Claims claims = parseClaims(accessToken);

        // 권한 정보가 없는 경우 예외를 발생시킵니다.
        if (claims.get("roles") == null) {
            throw new InvalidTokenException("권한 정보가 없는 토큰입니다.");
        }

        // 권한 정보를 문자열로 변환합니다.
        String rolesStr = claims.get("roles").toString();
        Collection<? extends GrantedAuthority> authorities;

        if (rolesStr.isEmpty()) {
            // 권한 정보가 비어있는 경우, 기본 권한을 설정합니다.
            authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_DEFAULT"));
        } else {
            // 권한 정보를 쉼표(,)로 분리하고 Spring Security의 GrantedAuthority로 변환합니다.
            authorities = Arrays.stream(rolesStr.split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }

        // UserDetails 객체를 생성하여 Authentication 객체를 반환합니다.
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }


    /**
     * 주어진 JWT 토큰의 유효성을 검증합니다.
     *
     * @param token 검증할 JWT 토큰 문자열.
     * @return 토큰이 유효한 경우 true, 그렇지 않은 경우 false 반환.
     * @throws InvalidTokenException 유효하지 않은 토큰인 경우 발생할 수 있는 사용자 정의 예외.
     */
    public boolean validateToken(String token) {
        try {
            // JWT 토큰을 검증하기 위한 파서를 생성합니다.
            // 파서는 사용자 지정 키(key)를 사용하여 토큰을 검증합니다.
            JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(key) // 사용자 지정 키로 서명을 검증합니다.
                    .build();

            // parseClaimsJws 메서드를 사용하여 JWT 토큰을 검증합니다.
            parser.parseClaimsJws(token);

            // 토큰이 유효한 경우 true를 반환합니다.
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            // 유효하지 않은 토큰 또는 형식 오류가 있는 경우 예외를 처리하고 사용자 정의 예외를 던집니다.
            log.info("Invalid JWT Token", e);
            throw new InvalidTokenException("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            // 만료된 토큰에 대한 처리를 수행할 수 있으며, 필요한 경우 예외를 처리합니다.
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            // 지원되지 않는 JWT 토큰인 경우 예외를 처리하고 사용자 정의 예외를 던집니다.
            log.info("Unsupported JWT Token", e);
            throw new InvalidTokenException("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            // JWT 클레임 문자열이 비어있는 경우 예외를 처리하고 사용자 정의 예외를 던집니다.
            log.info("JWT claims string is empty.", e);
            throw new InvalidTokenException("JWT claims string is empty.", e);
        }

        // 토큰이 유효하지 않은 경우 false를 반환합니다.
        return false;
    }


    /**
     * 주어진 JWT 토큰을 파싱하여 Claims 객체를 반환합니다.
     * 이 메서드는 JWT 토큰의 유효성을 검증하고, 토큰 내부에 저장된 클레임(claim)들을 추출합니다.
     *
     * @param accessToken 파싱할 JWT 토큰 문자열.
     * @return 토큰에서 추출된 Claims 객체.
     * @throws io.jsonwebtoken.JwtException 토큰이 유효하지 않거나 파싱 중 문제가 발생한 경우.
     */
    private Claims parseClaims(String accessToken) {
        try {
            // JWT 토큰을 해석하기 위한 파서를 생성합니다.
            // 파서는 사용자 지정 키(key)를 사용하여 토큰을 검증합니다.
            JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(key) // 사용자 지정 키로 서명을 검증합니다.
                    .build();

            // parseClaimsJws 메서드를 사용하여 JWT 토큰을 해석하고, Jws<Claims> 객체를 반환합니다.
            Jws<Claims> jws = parser.parseClaimsJws(accessToken);

            // Jws 객체에서 클레임(Claims)을 추출하여 반환합니다.
            Claims claims = jws.getBody();

            // 만약 토큰이 만료된 경우, ExpiredJwtException 예외가 발생할 수 있습니다.
            // 이 예외를 처리하여 만료된 토큰에 포함된 클레임을 반환합니다.
            return claims;
        } catch (ExpiredJwtException e) {
            // 만료된 토큰의 클레임을 반환합니다.
            return e.getClaims();
        }
    }


}