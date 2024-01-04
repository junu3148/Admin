package vikinglab.lumen.dao;

import org.springframework.stereotype.Repository;
import vikinglab.lumen.vo.AdminUser;

@Repository
public class AdminDAO implements AdminRepository {

    @Override
    public AdminUser login(AdminUser adminUser) {
        System.out.println("AdminDAO login()");

        AdminUser adminUserDB = new AdminUser();
        adminUserDB.setId("test");
        adminUserDB.setPassword("123");
        adminUserDB.setIsAdmin(1);

        return adminUser.getId().equals(adminUserDB.getId()) && adminUser.getPassword().equals(adminUserDB.getPassword()) ? adminUserDB : null;

    }

    @Override
    public AdminUser loginck(AdminUser adminUser) {
        System.out.println("AdminDAO loginck()");

        AdminUser adminUserDB = new AdminUser();
        adminUserDB.setId("test");
        adminUserDB.setPassword("123");
        adminUserDB.setIsAdmin(1);
        adminUserDB.setUserName("김동규");
        adminUserDB.setAccountName("신한은행");
        adminUserDB.setAccountNumber("111");

        return adminUser.getUserName().equals(adminUserDB.getUserName()) ? adminUserDB : null;
    }

    @Override
    public AdminUser loginck(String userName) {

        AdminUser adminUserDB = new AdminUser();
        adminUserDB.setUserName("임정민");
        adminUserDB.setId("test");
        adminUserDB.setPassword("123");
        adminUserDB.setIsAdmin(0);

        return userName.equals(adminUserDB.getUserName()) ? adminUserDB : null;
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