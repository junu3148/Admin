package com.lumen.www.controller;

import com.lumen.www.dto.user.AdminUser;
import com.lumen.www.dto.common.JsonResult;
import com.lumen.www.jwt.JwtTokenProvider;
import com.lumen.www.service.AdminService;
import com.lumen.www.service.MemberService;
import com.lumen.www.util.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return memberService.signInAndGenerateJwtToken(adminUser);
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

    // 관리자 정보 수정
    @PostMapping("admin-user/update")
    public ResponseEntity<?> adminUserUpdate(@RequestBody AdminUser adminUser) {
        return adminService.updateAdminUser(adminUser);
    }

    // accessToken 검증
    @PostMapping("access-token")
    public ResponseEntity<?> accessTokenCK(HttpServletRequest request) {
        return memberService.accessTokenCK(request);
    }


    // refreshToken 검증
    @PostMapping("refresh-token")
    public ResponseEntity<?> refreshTokenCK(HttpServletRequest request) {
        System.out.println("호출되니");
        return memberService.refreshTokenCK(JwtTokenUtil.resolveToken(request));
    }

    // logout
    @PostMapping("logout")
    public void adminLogout(HttpServletRequest request) {
        adminService.logout(request);
    }


}
