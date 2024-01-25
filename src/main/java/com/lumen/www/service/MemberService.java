package com.lumen.www.service;

import com.lumen.www.dao.AdminRepository;
import com.lumen.www.dao.TokenRepository;
import com.lumen.www.dto.AdminUser;
import com.lumen.www.dto.JwtToken;
import com.lumen.www.dto.RefreshToken;
import com.lumen.www.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {
    private final AdminRepository adminRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");

    @Transactional
    public ResponseEntity<?> signInAndGenerateJwtToken(AdminUser adminUser) {


        String username = adminUser.getUsername();
        String password = adminUser.getPassword();
        // 이메일 형싱 체크
        if (isValidEmail(username)) {
            try {
                // 1. username + password 를 기반으로 Authentication 객체 생성
                // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

                // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
                // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
                Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);


                // 3. 인증 정보를 기반으로 JWT 토큰 생성
                JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

                // 이부분 자체권환으로 하는방법이 있을텐데... 추후에 리펙토링 해야함.( 하지만 프론트에서 권한이필요해서 그냥 사용할 예정)
                jwtToken.setRole(adminRepository.getRole(username));

                // HTTP 응답 헤더에 JWT 토큰을 추가
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add("Authorization", "Bearer " + jwtToken.getAccessToken());

                return new ResponseEntity<>(jwtToken, httpHeaders, HttpStatus.OK);
            } catch (AuthenticationException e) {
                // 사용자 인증 실패 시 처리
                HttpHeaders httpHeaders = new HttpHeaders();
                return new ResponseEntity<>("인증에 실패하였습니다.", httpHeaders, HttpStatus.UNAUTHORIZED);
            }
        } else {
            HttpHeaders httpHeaders = new HttpHeaders();
            return new ResponseEntity<>("아이디는 이메일 형식이어야 합니다.", httpHeaders, HttpStatus.BAD_REQUEST);

        }
    }

    public ResponseEntity<?> refreshTokenCK(String refreshToken) {
        try {
            // 리프레시 토큰 유효성 검사
            if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
            }

            // 데이터베이스에서 리프레시 토큰 확인
            Optional<RefreshToken> tokenData = tokenRepository.refreshTokenCK(refreshToken);
            if (tokenData.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
            }

            // 새로운 액세스 토큰 생성
            String newAccessToken = jwtTokenProvider.generateAccessToken(tokenData);

            JwtToken jwtToken = new JwtToken();
            jwtToken.setAccessToken(newAccessToken);

            // HTTP 응답 헤더에 JWT 토큰을 추가
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "Bearer " + newAccessToken);

            // 새로운 액세스 토큰을 응답으로 반환
            return new ResponseEntity<>(jwtToken, httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the refresh token");
        }
    }



    // 이메일 형식 체크
    private boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

}
