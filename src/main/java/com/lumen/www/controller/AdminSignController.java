package com.lumen.www.controller;

import com.lumen.www.dto.AdminUser;
import com.lumen.www.dto.JsonResult;
import com.lumen.www.service.AdminService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class AdminSignController {

    private final AdminService adminService;
    private static final Logger logger = LoggerFactory.getLogger(AdminSignController.class);

    // 관리자 로그인 폼을 반환하는 엔드포인트
    @GetMapping("/adminlogin")
    public String adminLoginForm() {
        return "adminLoginForm";
    }

    // 1차 로그인 처리를 위한 엔드포인트
    @PostMapping("/admin-login")
    public JsonResult adminLogin(@RequestBody AdminUser adminUser) {
        return adminService.adminLogin(adminUser);
    }

    // 2차 로그인 처리를 위한 엔드포인트
    @PostMapping("/admin-login-ck")
    public ResponseEntity<String> adminLoginCk(@RequestBody AdminUser adminUser, HttpSession session) {
        try {
            // 서비스를 호출하여 2차 로그인 시도
            AdminUser adminUserDB = adminService.adminLoginCk(adminUser);
            logger.debug("controller: {}", adminUserDB);

            // 로그인이 성공했는지 검증
            if (adminUserDB != null) {
                // 로그인 성공 시, 세션에 사용자 정보 저장
                session.setAttribute("adminUser", adminUserDB);
                logger.debug("Session ID in AdminController: {}", session.getId());

                // HTTP 상태 코드 200 (OK)와 함께 성공 메시지 반환
                return ResponseEntity.ok("Exist");
            } else {
                // 로그인 실패 시, HTTP 상태 코드 401 (Unauthorized) 반환
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
            }
        } catch (Exception e) {
            // 예외 발생 시, 해당 예외를 처리하는 메소드 호출
            return handleException(e);
        }
    }

    // 로그아웃 처리를 위한 엔드포인트
    @GetMapping("/admin-logout")
    public ResponseEntity<String> adminLogout(HttpSession session) {
        session.removeAttribute("adminUser");
        session.invalidate();
        return ResponseEntity.ok("Logged out");
    }

    // 예외 처리 및 로그 출력을 담당하는 메서드
    private ResponseEntity<String> handleException(Exception e) {
        logger.error("Exception occurred: {}", e.getMessage(), e); // 예외 메시지 출력
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred"); // 내부 서버 오류 응답
    }
}



  /*  // 2차 로그인 세션에 토큰저장방법
    @PostMapping("/adminloginck")
    public ResponseEntity<String> login(@RequestBody AdminUser adminUser, HttpSession session) {
        String token = adminService.loginck(adminUser);

        if (token != null) {
            // 세션 쿠키에 토큰을 저장
            session.setAttribute("accessToken", token);

            // 세션 쿠키 만료 시간 설정 (예: 1800초)
            //session.setMaxInactiveInterval(1800);

            return ResponseEntity.ok("Exist");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }*/
/*    // 2차 로그인 서비스에서 쿠키반환
    @PostMapping("/adminloginck2")
    public ResponseEntity<String> login(@RequestBody AdminUser adminUser) {
        ResponseCookie accessCookie = adminService.loginck2(adminUser);

        if (accessCookie != null) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                    .body("Exist");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }*/
 /*    // 2차 로그인 쿠키에 토큰저장
   @PostMapping("/adminloginck")
    public ResponseEntity<String> login(@RequestBody AdminUser adminUser, HttpServletResponse response) {
        String token = adminService.loginck(adminUser);

        if (token != null) {
            // 토큰을 쿠키에 담아서 클라이언트에게 전달
            ResponseCookie accessCookie = ResponseCookie.from("accessToken", token)
                    .httpOnly(true)
                    .sameSite("None")
                    .secure(true)
                    .path("/")
                    .maxAge(1800)
                    .domain("192.168.0.16")
                    .build();

            //return ResponseEntity.ok(token);
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                    .body("Exist");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }*/



