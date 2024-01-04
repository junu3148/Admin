package vikinglab.lumen.service;

import org.springframework.http.ResponseCookie;
import vikinglab.lumen.vo.AdminUser;

public interface AdminService {

    // 유저 로그인
    AdminUser login(AdminUser adminUser);

    String loginck(AdminUser adminUser);

    ResponseCookie loginck2(AdminUser adminUser);

}