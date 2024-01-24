package com.lumen.www.controller;

import com.lumen.www.dto.AdminUser;
import com.lumen.www.dto.JwtToken;
import com.lumen.www.jwt.JwtTokenProvider;
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
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("admin/")
public class AuthController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("login")
    public ResponseEntity<?> signIn(@RequestBody AdminUser adminUser) {

        ResponseEntity<?> responseEntity = memberService.signInAndGenerateJwtToken(adminUser);
        // 인증 성공
        if (responseEntity.getStatusCode() == HttpStatus.OK) return responseEntity;
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
    }


    @PostMapping("login-ck")
    public ResponseEntity<String> adminLoginCk(@RequestBody AdminUser adminUser, HttpServletRequest request) {

        // 토큰에서 "Bearer " 접두어 제거
        String jwtToken = JwtTokenUtil.resolveToken(request);

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

        return ResponseEntity.ok().

                body("Exist");
    }


    @PostMapping("/test")
    public String test() {
        return "success";
    }


}
