package com.lumen.www;

import com.lumen.www.dao.DBRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import com.lumen.www.dao.AdminRepository;
import com.lumen.www.service.AdminService;
import com.lumen.www.service.AdminServiceImpl;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "com.lumen.www")
public class AppConfig {

    @Autowired
    private SqlSession sqlSession;

    @Value("${jwt.secret}")
    private String secretKey;

    // AdminService 빈 정의
    @Bean
    public AdminService adminService() {
        // AdminRepository를 의존성으로 주입하여 AdminServiceImpl 생성 및 반환
        return new AdminServiceImpl(adminRepository());
    }

    // AdminRepository 빈 정의
    @Bean
    public AdminRepository adminRepository() {
        // SqlSession을 의존성으로 주입하여 DBRepository 생성하여 반환
        return new DBRepository(sqlSession);
    }


    // JwtTokenProvider 빈 정의 (필요에 따라 주석 해제할 수 있음)
    /*@Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(secretKey);
    }*/
}
