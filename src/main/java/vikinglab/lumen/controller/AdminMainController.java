package vikinglab.lumen.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vikinglab.lumen.json.JsonResult;
import vikinglab.lumen.service.AdminService;
import vikinglab.lumen.service.AdminServiceImpl;

@RestController
@RequiredArgsConstructor
public class AdminMainController {

    private final AdminService adminService;

    // 관리자페이지 메인 (연결안됨)
    @GetMapping("/admin-main")
    public String adminMain(HttpSession session) {
        System.out.println("adminMain()");
        return "adminMain";
    }

    // 가입자 현황
    @GetMapping("/subscriber-count")
    public JsonResult subscriberCount() {
        return adminService.subscriberCount();
    }

    // 메인페이지 월별가입자 그래프
    @GetMapping("monthly-sales-chart")
    public JsonResult monthlySalesChart() {
        return adminService.getMonthlySalesChart();
    }

    // 메인페이지 현황지표
    @GetMapping("current-situation")
    public JsonResult currentSituation() {
        return adminService.getCurrentSituation();
    }

    // 메인페이지 문의현황
    @GetMapping("main-inquiry-list")
    public JsonResult mainInquiryList() {
        return adminService.getMainInquiryList();
    }
}
