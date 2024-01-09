package com.lumen.www.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.lumen.www.json.JsonResult;
import com.lumen.www.service.AdminService;
import com.lumen.www.vo.AdminUser;


@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAdminLoginForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/adminlogin"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("adminLoginForm"));
    }

    @Test
    public void testAdminLogin() throws Exception {
        AdminUser adminUser = new AdminUser();
        adminUser.setId("test");
        adminUser.setPassword("123");

        JsonResult expectedResponse = new JsonResult();
        expectedResponse.success("Logged in");

        Mockito.when(adminService.adminLogin(Mockito.any(AdminUser.class))).thenReturn(expectedResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/admin-login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(adminUser)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    public void testAdminLoginCkSuccess() throws Exception {
        AdminUser adminUser = new AdminUser();
        adminUser.setId("test");
        adminUser.setPassword("123");

        AdminUser adminUserDB = new AdminUser();
        adminUserDB.setId("test");
        adminUserDB.setIsAdmin(1);

        Mockito.when(adminService.adminLoginCk(Mockito.any(AdminUser.class))).thenReturn(adminUserDB);

        mockMvc.perform(MockMvcRequestBuilders.post("/admin-login-ck")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(adminUser))
                        .sessionAttr("adminUser", new AdminUser()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Exist"));
    }

    @Test
    public void testAdminLoginCkFailure() throws Exception {
        AdminUser adminUser = new AdminUser();
        adminUser.setId("test");
        adminUser.setPassword("123");

        Mockito.when(adminService.adminLoginCk(Mockito.any(AdminUser.class))).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/admin-login-ck")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(adminUser)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().string("Authentication failed"));
    }

    @Test
    public void testAdminLogout() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin-logout"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Logged out"));
    }
}
