package vikinglab.lumen.service;

import vikinglab.lumen.dto.JwtToken;
import vikinglab.lumen.vo.AdminUser;

public interface AdminService {

    // 유저 로그인
    AdminUser login(AdminUser adminUser);

    AdminUser loginck(AdminUser adminUser);

    // 회원가입
    AdminUser joinAdminUser(AdminUser adminUser);

}