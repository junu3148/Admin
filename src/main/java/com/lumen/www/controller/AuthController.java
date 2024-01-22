package com.lumen.www.controller;

import com.lumen.www.dto.AdminUser;
import com.lumen.www.dto.JwtToken;
import com.lumen.www.jwt.JwtTokenProvider;
import com.lumen.www.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.parser.Token;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/admin-login")
    public ResponseEntity<?> signIn(@RequestBody AdminUser adminUser) {
        System.out.println("1차 로그인");

        String username = adminUser.getUsername();
        String password = adminUser.getPassword();

        // 사용자 인증을 수행하고 JWT 토큰을 얻는 로직
        JwtToken jwtToken = memberService.signIn(username, password);

        if (jwtToken != null) {
            log.info("request username = {}", username);
            log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());

            // HTTP 응답 헤더에 JWT 토큰을 추가
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "Bearer " + jwtToken.getAccessToken());

            // HTTP 상태 코드 200 OK와 함께 JWT 토큰을 응답
            return new ResponseEntity<>(jwtToken, httpHeaders, HttpStatus.OK);
        } else {
            // 사용자 인증 실패 시 HTTP 상태 코드 401 Unauthorized 응답
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }


    @PostMapping("/admin-login-ck")
    public ResponseEntity<String> adminLoginCk(@RequestBody AdminUser adminUser, @RequestHeader("Authorization") String tokenHeader) {
        // 토큰에서 "Bearer " 접두어 제거
        String jwtToken = tokenHeader.substring(7);

        // JwtTokenProvider를 사용하여 Authentication 객체를 가져옴
        Authentication authentication = jwtTokenProvider.getAuthentication(jwtToken);

        // Authentication 객체에서 권한 정보(List<String>) 추출
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        System.out.println("AdminUser: " + adminUser);
        System.out.println("JWT Token: " + jwtToken);
        System.out.println("Roles: " + roles);

        // 로직 처리...

        return ResponseEntity.ok().body("Exist");
    }


    @PostMapping("/test")
    public String test() {
        return "success";
    }


}
