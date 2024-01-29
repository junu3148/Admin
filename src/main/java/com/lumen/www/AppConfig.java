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
import com.lumen.www.service.UserCleanupTask;
import com.lumen.www.util.EmailService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Configuration
@ComponentScan(basePackages = "com.lumen.www")
public class AppConfig {

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
            return new AdminServiceImpl(adminRepository(), emailService());
    }

    // AdminRepository 빈 정의
    @Bean
    public AdminRepository adminRepository() {
            return new AdminRepositoryImpl(sqlSession);
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
        return new MemberService(adminRepository, authManagerBuilder, jwtTokenProvider,tokenRepository);
    }

    // 0시 주기적인 메서드 실행 클래스
    @Bean
    public UserCleanupTask userCleanupTask() {
        return new UserCleanupTask(adminRepository());
    }

}


