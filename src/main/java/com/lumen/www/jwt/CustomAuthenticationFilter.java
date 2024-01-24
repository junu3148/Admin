package com.lumen.www.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lumen.www.dto.AdminUser;
import com.lumen.www.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final MemberService memberService;
    private final ObjectMapper objectMapper;

    public CustomAuthenticationFilter(MemberService memberService, ObjectMapper objectMapper) {
        this.memberService = memberService;
        this.objectMapper = objectMapper;
        setFilterProcessesUrl("/admin/login"); // 로그인 URL 설정
    }
/*
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            AdminUser adminUser = objectMapper.readValue(request.getInputStream(), AdminUser.class);
            return memberService.attemptAuthentication(adminUser.getAdminId(), adminUser.getPassword());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) {
        // MemberService를 사용하여 JWT 토큰 생성 및 응답 설정
        memberService.onSuccessfulAuthentication(request, response, authResult);
    }*/

    // 필요한 경우, 인증 실패 시 로직을 여기에 추가
}
