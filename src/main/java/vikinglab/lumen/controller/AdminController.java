package vikinglab.lumen.controller;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vikinglab.lumen.json.JsonResult;
import vikinglab.lumen.service.AdminServiceImpl;
import vikinglab.lumen.vo.AdminUser;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminServiceImpl adminService;

    @GetMapping("/adminlogin")
    public String adminloginform() {
        System.out.println("adminlogin()");
        return "adminloginform";
    }

    // 1차 로그인
    @PostMapping("/adminlogin")
    @ResponseBody
    public JsonResult adminLogin(@RequestBody AdminUser adminUser) {
        System.out.println("adminlogin()");

        AdminUser adminUserDB = adminService.login(adminUser);

        JsonResult jsonResult = new JsonResult();
        if (adminUserDB != null) {
            jsonResult.success(adminUserDB.getIsAdmin());
        } else {
            jsonResult.fail("로그인실패");
        }
        return jsonResult;
    }

    // 2차 로그인 서비스에서 쿠키반환
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
    }

    // 2차 로그인 세션에 토큰저장방법
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
    }

    // 2차 로그인 쿠키에 토큰저장
/*    @PostMapping("/adminloginck")
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


    // 로그아웃
    @GetMapping("/adminlogout")
    public ResponseEntity<String> logout(HttpSession session) {
        // 세션에서 토큰을 삭제
        session.removeAttribute("accessToken");

        // 세션을 무효화시킴
        session.invalidate();

        return ResponseEntity.ok("Logged out");
    }

}



