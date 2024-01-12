package com.lumen.www;

import com.lumen.www.dao.AdminRepository;
import com.lumen.www.dao.DBRepository;
import com.lumen.www.service.AdminService;
import com.lumen.www.service.AdminServiceImpl;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.lumen.www")
public class AppConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);
    private final SqlSession sqlSession;

    @Value("${jwt.secret}")
    private String secretKey;

    public AppConfig(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    // AdminService 빈 정의
    @Bean
    public AdminService adminService() {
        try {
            return new AdminServiceImpl(adminRepository());
        } catch (Exception e) {
            logger.error("Error creating AdminService bean", e);
            throw e; // 다시 예외를 던져서 호출자가 처리할 수 있도록 합니다.
        }
    }

    // AdminRepository 빈 정의
    @Bean
    public AdminRepository adminRepository() {
        try {
            return new DBRepository(sqlSession);
        } catch (Exception e) {
            logger.error("Error creating AdminRepository bean", e);
            throw e; // 다시 예외를 던져서 호출자가 처리할 수 있도록 합니다.
        }
    }


    // JwtTokenProvider 빈 정의 (필요에 따라 주석 해제할 수 있음)
    /*@Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(secretKey);
    }*/
}
