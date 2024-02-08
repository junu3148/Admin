package com.lumen.www.controller;

import com.lumen.www.dto.auth.JwtToken;
import com.lumen.www.dto.common.JsonResult;
import com.lumen.www.dto.user.AdminUser;
import com.lumen.www.service.AdminService;
import com.lumen.www.service.MemberService;
import com.lumen.www.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("admin/")
public class AuthController {

    private final MemberService memberService;
    private final AdminService adminService;

    // 1차 로그인 토큰발행
    @PostMapping("login")
    public ResponseEntity<JwtToken> signIn(@RequestBody AdminUser adminUser) {
        return memberService.signInAndGenerateJwtToken(adminUser);
    }

    // 2차 로그인 추가정보 확인
    @PostMapping("login-ck")
    public ResponseEntity<String> adminLoginCk(@RequestBody AdminUser adminUser) {
        return adminService.adminLoginCk(adminUser);
    }

    // 관리자 정보
    @PostMapping("account")
    public JsonResult getAccount(HttpServletRequest request) {
        return adminService.getAdminUser(request);
    }

    // 관리자 정보 수정
    @PatchMapping("account")
    public ResponseEntity<String> adminUserUpdate(@RequestBody AdminUser adminUser) {
        return adminService.updateAdminUser(adminUser);
    }

    // accessToken 검증
    @PostMapping("access-token") // 02.06 수정 토큰 유효성 검사에 유효시간 지난 에러 처리가 없었다.
    public String accessTokenCK() {
        return "accessTokenCk";
    }

    // refreshToken 검증
    @PostMapping("refresh-token")
    public ResponseEntity<String> refreshTokenCK(HttpServletRequest request) {
        return memberService.refreshTokenCK(JwtTokenUtil.resolveToken(request));
    }

    // logout
    @PostMapping("logout")
    public void adminLogout(HttpServletRequest request) {
        adminService.logout(request);
    }


}
