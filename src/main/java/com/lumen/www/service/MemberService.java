package com.lumen.www.service;

import com.lumen.www.dao.AdminRepository;
import com.lumen.www.dao.TokenRepository;
import com.lumen.www.dto.user.AdminUser;
import com.lumen.www.dto.auth.JwtToken;
import com.lumen.www.dto.auth.RefreshToken;
import com.lumen.www.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final AdminRepository adminRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String INVALID_TOKEN_MESSAGE = "Invalid or expired refresh token";
    private static final String ERROR_PROCESSING_MESSAGE = "An error occurred while processing the refresh token";
    private static final String INVALID_CREDENTIALS_MESSAGE = "인증에 실패하였습니다.";
    private static final String INVALID_EMAIL_MESSAGE = "아이디는 이메일 형식이어야 합니다.";


    /**
     * 사용자 로그인을 처리하고, JWT 토큰을 생성하여 반환하는 메서드입니다.
     *
     * @param adminUser 로그인에 사용할 관리자 사용자 정보를 포함하는 객체입니다.
     * @return 성공적으로 로그인하고 토큰을 생성한 경우, 해당 토큰을 포함하는 ResponseEntity를 반환합니다.
     *         유효하지 않은 이메일이 입력된 경우, BAD_REQUEST 상태와 메시지를 포함하는 ResponseEntity를 반환합니다.
     *         인증이 실패한 경우, UNAUTHORIZED 상태와 메시지를 포함하는 ResponseEntity를 반환합니다.
     */
    @Transactional
    public ResponseEntity<?> signInAndGenerateJwtToken(AdminUser adminUser) {
        String username = adminUser.getUsername();
        String password = adminUser.getPassword();

        if (!isValidEmail(username)) {
            return badRequestResponse();
        }

        try {
            JwtToken jwtToken = authenticateAndGenerateToken(username, password);
            return buildResponseWithToken(jwtToken);
        } catch (AuthenticationException e) {
            return unauthorizedResponse(INVALID_CREDENTIALS_MESSAGE);
        }
    }

    /**
     * 주어진 사용자 이름과 비밀번호를 사용하여 사용자를 인증하고, JWT 토큰을 생성하는 메서드입니다.
     *
     * @param username 사용자 이름입니다.
     * @param password 사용자 비밀번호입니다.
     * @return 생성된 JWT 토큰입니다.
     */
    private JwtToken authenticateAndGenerateToken(String username, String password) {
        Authentication authentication = authenticateUser(username, password);
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);
        jwtToken.setRole(adminRepository.getRole(username));
        return jwtToken;
    }

    /**
     * 사용자 이름과 비밀번호로 사용자를 인증하는 메서드입니다.
     *
     * @param username 사용자 이름입니다.
     * @param password 사용자 비밀번호입니다.
     * @return 인증된 사용자의 Authentication 객체입니다.
     */
    private Authentication authenticateUser(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManagerBuilder.getObject().authenticate(authenticationToken);
    }

    /**
     * JWT 토큰을 포함한 ResponseEntity를 생성하여 반환하는 메서드입니다.
     *
     * @param jwtToken JWT 토큰 객체입니다.
     * @return JWT 토큰을 포함한 ResponseEntity입니다.
     */
    private ResponseEntity<?> buildResponseWithToken(JwtToken jwtToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION_HEADER, BEARER_PREFIX + jwtToken.getAccessToken());
        return new ResponseEntity<>(jwtToken, httpHeaders, HttpStatus.OK);
    }

    /**
     * 제공된 토큰을 사용하여 JWT 토큰을 포함한 ResponseEntity를 생성하여 반환하는 메서드입니다.
     *
     * @param token JWT 액세스 토큰입니다.
     * @return JWT 토큰을 포함한 ResponseEntity입니다.
     */
    private ResponseEntity<?> buildResponseWithToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION_HEADER, BEARER_PREFIX + token);

        JwtToken jwtToken = new JwtToken();
        jwtToken.setAccessToken(token);

        return new ResponseEntity<>(jwtToken, headers, HttpStatus.OK);
    }

    /**
     * 잘못된 요청에 대한 응답을 생성하여 반환하는 메서드입니다.
     *
     * @return BAD_REQUEST 상태와 메시지를 포함한 ResponseEntity입니다.
     */
    private ResponseEntity<String> badRequestResponse() {
        HttpHeaders httpHeaders = new HttpHeaders();
        return new ResponseEntity<>(MemberService.INVALID_EMAIL_MESSAGE, httpHeaders, HttpStatus.BAD_REQUEST);
    }

    /**
     * 리프레시 토큰의 유효성을 검사한 후, 새로운 액세스 토큰을 발행하여 반환하는 메서드입니다.
     *
     * @param refreshToken 리프레시 토큰입니다.
     * @return 새로운 액세스 토큰을 포함하는 ResponseEntity를 반환합니다.
     *         유효하지 않은 리프레시 토큰인 경우, UNAUTHORIZED 상태와 메시지를 포함하는 ResponseEntity를 반환합니다.
     *         예외가 발생한 경우, INTERNAL_SERVER_ERROR 상태와 메시지를 포함하는 ResponseEntity를 반환합니다.
     */
    public ResponseEntity<?> refreshTokenCK(String refreshToken) {
        try {
            if (!isValidRefreshToken(refreshToken)) {
                return unauthorizedResponse(INVALID_TOKEN_MESSAGE);
            }

            String newAccessToken = createNewAccessToken(refreshToken);
            return buildResponseWithToken(newAccessToken);
        } catch (Exception e) {
            return internalServerErrorResponse();
        }
    }

    /**
     * 제공된 리프레시 토큰이 유효한지 검사하는 메서드입니다.
     *
     * @param refreshToken 검사할 리프레시 토큰입니다.
     * @return 토큰이 유효하면 true, 그렇지 않으면 false를 반환합니다.
     */
    private boolean isValidRefreshToken(String refreshToken) {
        return refreshToken != null && jwtTokenProvider.validateToken(refreshToken)
                && tokenRepository.refreshTokenCK(refreshToken).isPresent();
    }

    /**
     * 제공된 리프레시 토큰을 사용하여 새로운 액세스 토큰을 생성하는 메서드입니다.
     *
     * @param refreshToken 액세스 토큰을 생성하기 위한 리프레시 토큰입니다.
     * @return 생성된 새로운 액세스 토큰입니다.
     */
    private String createNewAccessToken(String refreshToken) {
        Optional<RefreshToken> tokenData = tokenRepository.refreshTokenCK(refreshToken);
        return jwtTokenProvider.generateAccessToken(tokenData);
    }

    /**
     * 인증되지 않은 요청에 대한 응답을 생성하여 반환하는 메서드입니다.
     *
     * @param message 응답 메시지입니다.
     * @return UNAUTHORIZED 상태와 메시지를 포함한 ResponseEntity입니다.
     */
    private ResponseEntity<String> unauthorizedResponse(String message) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }

    /**
     * 내부 서버 오류에 대한 응답을 생성하여 반환하는 메서드입니다.
     *
     * @return INTERNAL_SERVER_ERROR 상태와 메시지를 포함한 ResponseEntity입니다.
     */
    private ResponseEntity<String> internalServerErrorResponse() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(MemberService.ERROR_PROCESSING_MESSAGE);
    }





    // 로그인 토큰 생성
//   @Transactional
//    public ResponseEntity<?> signInAndGenerateJwtToken(AdminUser adminUser) {
//
//        String username = adminUser.getUsername();
//        String password = adminUser.getPassword();
//
//        // 이메일 형싱 체크
//        if (isValidEmail(username)) {
//            try {
//                // 1. username + password 를 기반으로 Authentication 객체 생성
//                // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
//                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
//
//                // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
//                // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
//                Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//
//                // 3. 인증 정보를 기반으로 JWT 토큰 생성
//                JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);
//                System.out.println(jwtToken);
//
//                // 이부분 자체권환으로 하는방법이 있을텐데... 추후에 리펙토링 해야함.( 하지만 프론트에서 권한이필요해서 그냥 사용할 예정)
//                jwtToken.setRole(adminRepository.getRole(username));
//
//
//                // HTTP 응답 헤더에 JWT 토큰을 추가
//                HttpHeaders httpHeaders = new HttpHeaders();
//                httpHeaders.add(AUTHORIZATION_HEADER, BEARER_PREFIX + jwtToken.getAccessToken());
//
//                return new ResponseEntity<>(jwtToken, httpHeaders, HttpStatus.OK);
//            } catch (AuthenticationException e) {
//                // 사용자 인증 실패 시 처리
//                HttpHeaders httpHeaders = new HttpHeaders();
//                return new ResponseEntity<>("인증에 실패하였습니다.", httpHeaders, HttpStatus.UNAUTHORIZED);
//            }
//        } else {
//            HttpHeaders httpHeaders = new HttpHeaders();
//            return new ResponseEntity<>("아이디는 이메일 형식이어야 합니다.", httpHeaders, HttpStatus.BAD_REQUEST);
//
//        }
//    }

   /* @Transactional
    // 리플레시 토큰 유효성검사 후 에세스토큰 발행
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
    }*/

    // 이메일 형식 체크
    private boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

}
