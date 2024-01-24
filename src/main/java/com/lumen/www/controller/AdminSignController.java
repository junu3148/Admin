package com.lumen.www.controller;

import com.lumen.www.service.AdminService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class AdminSignController {

    private final AdminService adminService;

    // 관리자 로그인 폼을 반환하는 엔드포인트
    @GetMapping("/adminlogin")
    public String adminLoginForm() {
        return "adminLoginForm";
    }

    // 로그아웃 처리를 위한 엔드포인트
    @GetMapping("/admin-logout")
    public ResponseEntity<String> adminLogout(HttpSession session) {
        session.removeAttribute("adminUser");
        session.invalidate();
        return ResponseEntity.ok("Logged out");
    }


}

