package vikinglab.lumen.dao;

import org.springframework.stereotype.Repository;
import vikinglab.lumen.vo.AdminUser;

@Repository
public class AdminDAO implements AdminRepository {

    @Override
    public AdminUser login(AdminUser adminUser) {
        System.out.println("AdminDAO login()");

        AdminUser adminUser1 = new AdminUser();
        adminUser1.setId("test");
        adminUser1.setPassword("123");
        adminUser1.setIsAdmin(1);

        return adminUser.getId().equals(adminUser1.getId()) ? adminUser1 : null;

    }

    @Override
    public AdminUser loginck(AdminUser adminUser) {
        System.out.println("AdminDAO loginck()");

        System.out.println(adminUser);

        AdminUser adminUser1 = new AdminUser();
        adminUser1.setId("test");
        adminUser1.setPassword("123");
        adminUser1.setIsAdmin(1);
        adminUser1.setUserName("김동규");
        adminUser1.setAccountName("신한은행");
        adminUser1.setAccountNumber("111");

        return adminUser.getUserName().equals(adminUser1.getUserName()) ? adminUser1 : null;
    }

    @Override
    public AdminUser loginck(String userName) {

        AdminUser adminUser1 = new AdminUser();
        adminUser1.setUserName("임정민");
        adminUser1.setId("test");
        adminUser1.setPassword("123");
        adminUser1.setIsAdmin(0);

        return userName.equals(adminUser1.getUserName()) ? adminUser1 : null;
    }

    @Override
    public int idCheck(AdminUser adminUser) {
        return 0;
    }

    @Override
    public AdminUser joinAdminUser(AdminUser adminUser) {
        return null;
    }
}