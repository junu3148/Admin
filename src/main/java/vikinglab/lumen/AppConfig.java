package vikinglab.lumen;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import vikinglab.lumen.dao.MemoryAdminRepository;
import vikinglab.lumen.dao.AdminRepository;
import vikinglab.lumen.jwt.JwtTokenProvider;
import vikinglab.lumen.service.AdminService;
import vikinglab.lumen.service.AdminServiceImpl;

@Configuration
@ComponentScan(basePackages = "vikinglab.lumen")
public class AppConfig {

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
        // MemoryAdminRepository를 생성하여 반환
        return new MemoryAdminRepository();
    }

/*    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(secretKey);
    }*/
}

