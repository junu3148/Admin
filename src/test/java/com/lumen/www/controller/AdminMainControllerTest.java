package com.lumen.www.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.lumen.www.json.JsonResult;
import com.lumen.www.service.AdminService;
import com.lumen.www.vo.AdminUser;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AdminMainControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AdminService adminService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new AdminMainController(adminService)).build();
    }

    @Test
    public void testAdminMain() throws Exception {
        mockMvc.perform(get("/admin-main"))
                .andExpect(status().isOk())
                .andExpect(content().string("adminMain"));
    }

    @Test
    public void testSubscriberCount() throws Exception {
        MockHttpSession session = new MockHttpSession();
        AdminUser adminUser = new AdminUser();
        session.setAttribute("adminUser", adminUser);

        JsonResult expectedResult = new JsonResult();
        expectedResult.success(100); // 예상 결과값 설정

        when(adminService.subscriberCount()).thenReturn(expectedResult);

        mockMvc.perform(get("/subscriber-count").session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").value(100)); // 예상 결과값
    }


    @Test
    public void testMonthlySalesChart() throws Exception {
        when(adminService.getMonthlySalesChart()).thenReturn(new JsonResult(/* 예상 결과값 */));

        mockMvc.perform(get("/monthly-sales-chart"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // 여기서 실제 JSON 결과를 검증하도록 필요에 따라 추가 코드 작성
    }

    @Test
    public void testCurrentSituation() throws Exception {
        when(adminService.getCurrentSituation()).thenReturn(new JsonResult(/* 예상 결과값 */));

        mockMvc.perform(get("/current-situation"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // 여기서 실제 JSON 결과를 검증하도록 필요에 따라 추가 코드 작성
    }

    @Test
    public void testMainInquiryList() throws Exception {
        when(adminService.getMainInquiryList()).thenReturn(new JsonResult(/* 예상 결과값 */));

        mockMvc.perform(get("/main-inquiry-list"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // 여기서 실제 JSON 결과를 검증하도록 필요에 따라 추가 코드 작성
    }
}
