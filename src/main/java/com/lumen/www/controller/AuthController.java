package com.lumen.www.controller;

import com.lumen.www.dto.AdminUser;
import com.lumen.www.dto.JsonResult;
import com.lumen.www.dto.JwtToken;
import com.lumen.www.jwt.JwtTokenProvider;
import com.lumen.www.service.AdminService;
import com.lumen.www.service.MemberService;
import com.lumen.www.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
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

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("admin/")
public class AuthController {
    private final MemberService memberService;
    private final AdminService adminService;


    // 1차 로그인 토큰발행
    @PostMapping("login")
    public ResponseEntity<?> signIn(@RequestBody AdminUser adminUser) {
        ResponseEntity<?> responseEntity = memberService.signInAndGenerateJwtToken(adminUser);
        // 인증 성공
        if (responseEntity.getStatusCode() == HttpStatus.OK) return responseEntity;
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");

    }

    // 2차 로그인 추가정보 확인
    @PostMapping("login-ck")
    public ResponseEntity<?> adminLoginCk(@RequestBody AdminUser adminUser) {
        return adminService.adminLoginCk(adminUser);
    }

    // 관리자 정보
    @PostMapping("account")
    public JsonResult getAccount(HttpServletRequest request) {
        return adminService.getAdminUser(request);
    }

    // accessToken 검증
    @PostMapping("access-token")
    public ResponseEntity<?> accessTokenCK() {
        return ResponseEntity.ok().body("Request is valid and authenticated");
    }

    // refreshToken 검증
    @PostMapping("refresh-token")
    public ResponseEntity<?> refreshTokenCK(HttpServletRequest request) {

        String refreshToken = JwtTokenUtil.resolveToken(request);
        ResponseEntity<?> responseEntity = memberService.refreshTokenCK(refreshToken);

        // 인증 성공
        if (responseEntity.getStatusCode() == HttpStatus.OK) return responseEntity;
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");

    }


}
