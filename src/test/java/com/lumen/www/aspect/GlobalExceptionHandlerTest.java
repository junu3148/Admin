package com.lumen.www.aspect;

import com.lumen.www.AppConfig;
import com.lumen.www.controller.AdminController;
import com.lumen.www.controller.AuthController;
import com.lumen.www.exception.CustomException;
import com.lumen.www.service.AdminService;
import com.lumen.www.service.EmailService;
import com.lumen.www.service.InvoiceService;
import com.lumen.www.service.MemberService;
import com.lumen.www.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {AdminController.class, AuthController.class}) // 두 컨트롤러 모두 테스트에 포함
@Import({AppConfig.class}) // AppConfig를 테스트 컨텍스트에 명시적으로 포함 (필요한 경우)
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    // 필요한 서비스 및 컴포넌트를 MockBean으로 등록
    @MockBean
    private AdminService adminService;
    @MockBean
    private MemberService memberService;
    @MockBean
    private EmailService emailService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private InvoiceService invoiceService;

    @Test
    public void whenCustomExceptionThrown_thenBadRequest() throws Exception {
        // "/admin/test-exception" 엔드포인트는 CustomException을 발생시키는 가상의 엔드포인트
        mockMvc.perform(get("/admin/*"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Custom error: Your custom error message"));
    }
}
