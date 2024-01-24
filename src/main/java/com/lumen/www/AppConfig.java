package com.lumen.www;

import com.lumen.www.dao.AdminRepository;
import com.lumen.www.dao.AdminRepositoryImpl;
import com.lumen.www.dao.TokenRepository;
import com.lumen.www.files.FileStorageProperties;
import com.lumen.www.files.ImageUploader;
import com.lumen.www.jwt.JwtTokenProvider;
import com.lumen.www.service.AdminService;
import com.lumen.www.service.AdminServiceImpl;
import com.lumen.www.service.MemberService;
import com.lumen.www.util.EmailService;
import com.lumen.www.util.JwtTokenUtil;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Configuration
@ComponentScan(basePackages = "com.lumen.www")
public class AppConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);
    private final SqlSession sqlSession;
    private final JavaMailSender javaMailSender;

    @Value("${jwt.secret}")
    private String secretKey;

    public AppConfig(SqlSession sqlSession, JavaMailSender javaMailSender) {
        this.sqlSession = sqlSession;
        this.javaMailSender = javaMailSender;
    }

    // AdminService 빈 정의
    @Bean
    public AdminService adminService() {
        try {
            return new AdminServiceImpl(adminRepository(), emailService(), jwtTokenProvider());
        } catch (Exception e) {
            logger.error("Error creating AdminService bean", e);
            throw e; // 다시 예외를 던져서 호출자가 처리할 수 있도록 합니다.
        }
    }

    // AdminRepository 빈 정의
    @Bean
    public AdminRepository adminRepository() {
        try {
            return new AdminRepositoryImpl(sqlSession);
        } catch (Exception e) {
            logger.error("Error creating AdminRepository bean", e);
            throw e; // 다시 예외를 던져서 호출자가 처리할 수 있도록 합니다.
        }
    }

    // 파일 경로
    @Bean
    @ConfigurationProperties(prefix = "file")
    public FileStorageProperties fileStorageProperties() {
        return new FileStorageProperties();
    }

    // 이미지 업로드
    @Bean
    public ImageUploader imageUploader(FileStorageProperties fileStorageProperties) {
        return new ImageUploader(fileStorageProperties);
    }

    // EmailService
    @Bean
    public EmailService emailService() {
        return new EmailService(javaMailSender, new SpringTemplateEngine());
    }


    // JwtTokenProvider 빈 정의
    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(secretKey, tokenRepository());
    }

    // TokenRepository 빈 정의
    @Bean
    public TokenRepository tokenRepository() {
        return new TokenRepository(sqlSession);
    }

    // AuthenticationManagerBuilder를 주입받아 사용
    @Bean
    public MemberService memberService(AdminRepository adminRepository, AuthenticationManagerBuilder authManagerBuilder, JwtTokenProvider jwtTokenProvider, TokenRepository tokenRepository) {
        return new MemberService(adminRepository, authManagerBuilder, jwtTokenProvider, tokenRepository);
    }


}


