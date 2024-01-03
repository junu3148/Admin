package vikinglab.lumen;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import vikinglab.lumen.dao.AdminDAO;
import vikinglab.lumen.dao.AdminRepository;
import vikinglab.lumen.service.AdminServiceImpl;

@Configuration
@ComponentScan(basePackages = "vikinglab.lumen")
public class AppConfig {

    @Bean
    public AdminServiceImpl adminService() {
        return new AdminServiceImpl(adminRepository());
    }

    @Bean
    public AdminRepository adminRepository() {

        return new AdminDAO();
    }

}