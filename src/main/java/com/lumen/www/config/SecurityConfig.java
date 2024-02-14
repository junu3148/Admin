package com.lumen.www.config;

import com.lumen.www.exception.JwtAccessDeniedHandler;
import com.lumen.www.exception.JwtAuthenticationEntryPoint;
import com.lumen.www.jwt.JwtAuthenticationFilter;
import com.lumen.www.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
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

    public final String URL = "http://192.168.0.16:3000"; // http://www.lumen.com

    // Spring Security의 필터 체인을 구성합니다.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.httpBasic(HttpBasicConfigurer::disable);
        http.csrf(CsrfConfigurer::disable);
        http.sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.cors(Customizer.withDefaults());


        // 요청 경로에 대한 권한 설정
        http.authorizeHttpRequests((authz) -> authz
                .requestMatchers("/admin/login", "/admin/main/*", "/admin/refresh-token", "/authToken", "/upload/**").permitAll()
                .requestMatchers("/admin/**").permitAll()
                .anyRequest().authenticated());

        http.exceptionHandling(authenticationManager -> authenticationManager
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler));

        // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 전에 추가
        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);


        // CSP, Frame Options, HSTS 등의 보안 헤더 설정
        http.headers(headers -> headers
                .contentSecurityPolicy(csp -> csp.policyDirectives(CSP_POLICY))
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)
                .httpStrictTransportSecurity(hsts -> hsts
                        .includeSubDomains(true)
                        .maxAgeInSeconds(31536000)
                        .preload(true))
        );




/*

        // https 만 접근허용 나중이야기
        http
                // HTTPS 강제 사용 구성
                .requiresChannel(channel -> channel
                        .requestMatchers(r -> "https".equals(r.getHeader("X-Forwarded-Proto")))
                        .requiresSecure());

*/


        // HttpSecurity 설정을 기반으로 SecurityFilterChain 빈 생성 및 반환
        return http.build();
    }


    // CORS 정책을 구성합니다.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(URL));
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