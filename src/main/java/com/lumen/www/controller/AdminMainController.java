package com.lumen.www.controller;

import com.lumen.www.dto.common.JsonResult;
import com.lumen.www.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("admin/main/")
public class AdminMainController {

    private final AdminService adminService;

    // 가입자 현황
    @GetMapping("subscriber")
    public JsonResult getSubscriberCount() {
        return adminService.getSubscriberCount();
    }

    // 메인페이지 월별가입자 그래프
    @GetMapping("chart")
    public JsonResult getMonthlySalesChart() {
        return adminService.getMonthlySalesChart();
    }

    // 메인페이지 현황지표
    @GetMapping("indicators")
    public JsonResult getCurrentSituation() {
        return adminService.getCurrentSituation();
    }

    // 메인페이지 문의현황
    @GetMapping("inquiry")
    public JsonResult getMainInquiryList() {
        return adminService.getMainInquiryList();
    }
}
