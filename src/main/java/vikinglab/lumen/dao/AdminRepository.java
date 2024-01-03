package vikinglab.lumen.dao;

import vikinglab.lumen.vo.AdminUser;

public interface AdminRepository {

    // 관리자 로그인
    AdminUser login(AdminUser adminUser);

    // 관리자 로그인 2차검증
    AdminUser loginck(AdminUser adminUser);

    AdminUser loginck(String userName);

    // 아이디 체크
    int idCheck(AdminUser adminUser);

    // 회원가입
    AdminUser joinAdminUser(AdminUser adminUser);


}