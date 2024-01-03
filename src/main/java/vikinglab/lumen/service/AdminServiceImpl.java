package vikinglab.lumen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vikinglab.lumen.dao.AdminRepository;
import vikinglab.lumen.dto.JwtToken;
import vikinglab.lumen.vo.AdminUser;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminDAO;


    @Override
    public AdminUser login(AdminUser adminUser) {
        System.out.println("AdminSeviceImpl login()");

        return adminDAO.login(adminUser);

    }

/*    @Override
    public JwtToken loginck(AdminUser adminUser) {
        System.out.println("AdminSeviceImpl loginck()");

        AdminUser userFromDB = adminDAO.loginck(adminUser);

        if (userFromDB != null && userFromDB.getPassword().equals(adminUser.getPassword())) {
            // 사용자 인증이 성공하면 토큰 생성
            return jwtTokenProvider.generateToken(userFromDB);
        } else {
            throw new org.springframework.security.core.AuthenticationException("Authentication failed") {
            };
        }
    }*/

    @Override
    public AdminUser loginck(AdminUser adminUser) {
        System.out.println("AdminSeviceImpl loginck()");


        System.out.println(adminUser);


        // 파라미터가 하나일꺼다 조건을 다른게 null일때 로그인2 체키해야함

        return adminUser.getAccountName() != null ? adminDAO.loginck(adminUser) : adminDAO.loginck(adminUser.getUserName());
    }

    @Override
    public AdminUser joinAdminUser(AdminUser adminUser) {
        return null;
    }
}