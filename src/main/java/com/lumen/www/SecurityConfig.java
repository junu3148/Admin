package com.lumen.www;

import com.lumen.www.exception.JwtAccessDeniedHandler;
import com.lumen.www.exception.JwtAuthenticationEntryPoint;
import com.lumen.www.jwt.JwtAuthenticationFilter;
import com.lumen.www.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public static final String CSP_POLICY = "default-src 'self'; " +
            "script-src 'self' 'unsafe-inline' 'unsafe-eval' https://trusteddomain.com; " +
            "style-src 'self' 'unsafe-inline' https://trusteddomain.com; " +
            "img-src 'self' data: https://trusteddomain.com; " +
            "connect-src 'self' https://api.trusteddomain.com; " +
            "font-src 'self'; " +
            "form-action 'self'; " +
            "frame-ancestors 'none';";

    public final String URI = "http://192.168.0.16:3000"; // http://www.lumen.com

    // Spring Security의 필터 체인을 구성합니다.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // HTTP 기본 인증 비활성화
        http.httpBasic().disable();
        // CSRF 보호 기능 비활성화
        http.csrf().disable();

        // 세션 관리를 STATELESS로 설정하여, 세션을 사용하지 않고 매 요청마다 인증
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // CORS 설정을 적용
        http.cors().configurationSource(corsConfigurationSource());

        // 요청 경로에 대한 권한 설정
        http.authorizeHttpRequests((authz) -> authz
                .requestMatchers("/admin/login", "/admin/main/*", "/admin/refresh-token", "/authToken", "/upload/**").permitAll()
                .requestMatchers("/admin/**").permitAll()
                .anyRequest().authenticated());

        // 인증 실패와 접근 거부 처리를 위한 핸들러 설정
        http.exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler);

        // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 전에 추가
        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        // CSP, Frame Options, HSTS 등의 보안 헤더 설정
        http.headers(headers -> headers
                .contentSecurityPolicy(CSP_POLICY)
                .and()
                .frameOptions().deny()
                .httpStrictTransportSecurity(hsts -> hsts
                        .includeSubDomains(true)
                        .maxAgeInSeconds(31536000)));

        // https 만 접근허용 나중이야기
        //http.requiresChannel().requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null).requiresSecure();

        // HttpSecurity 설정을 기반으로 SecurityFilterChain 빈 생성 및 반환
        return http.build();
    }


    // CORS 정책을 구성합니다.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(URI));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "DELETE"));
        configuration.setAllowCredentials(true); // 크리덴셜 허용
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 CORS 설정 적용
        return source;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 인코더로 BCrypt 사용
    }

}