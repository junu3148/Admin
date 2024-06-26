package com.lumen.www.config;

import com.lumen.www.dao.AdminRepository;
import com.lumen.www.dao.AdminRepositoryImpl;
import com.lumen.www.dao.TokenRepository;
import com.lumen.www.dao.TokenRepositoryImpl;
import com.lumen.www.files.FileStorageProperties;
import com.lumen.www.files.ImageUploader;
import com.lumen.www.jwt.JwtTokenProvider;
import com.lumen.www.service.*;
import com.lumen.www.service.EmailService;
import com.lumen.www.util.UserCleanupTask;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

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
        return new AdminServiceImpl(adminRepository(), jwtTokenProvider(), tokenRepository());
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

    // ImageUploader 빈 정의
    @Bean
    public ImageUploader imageUploader(FileStorageProperties fileStorageProperties) {
        return new ImageUploader(fileStorageProperties);
    }

    // EmailService 빈 정의
    @Bean
    public EmailService emailService() {
        return new EmailServiceImpl(javaMailSender, adminRepository());
    }

    // JwtTokenProvider 빈 정의
    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(secretKey, tokenRepository());
    }

    // TokenRepository 빈 정의
    @Bean
    public TokenRepository tokenRepository() {
        return new TokenRepositoryImpl(sqlSession);
    }

    // AuthenticationManagerBuilder를 주입받아 사용
    @Bean
    public MemberService memberService(AdminRepository adminRepository, AuthenticationManagerBuilder authManagerBuilder, JwtTokenProvider jwtTokenProvider, TokenRepository tokenRepository) {
        return new MemberServiceImpl(adminRepository, authManagerBuilder, jwtTokenProvider, tokenRepository);
    }

    // 0시 주기적인 메서드 실행 클래스
    @Bean
    public UserCleanupTask userCleanupTask() {
        return new UserCleanupTask(adminRepository());
    }

    // InvoiceService 빈 정의
    @Bean
    public InvoiceService invoiceService() {
        return new InvoiceServiceImpl(javaMailSender, adminRepository());
    }

}


