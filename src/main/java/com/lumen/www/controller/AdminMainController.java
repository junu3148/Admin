package com.lumen.www.controller;

import com.lumen.www.dto.JsonResult;
import com.lumen.www.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("admin/")
public class AdminMainController {

    private final AdminService adminService;

    // 가입자 현황
    @GetMapping("subscriber-count")
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
