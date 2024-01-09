package com.lumen.www.service;

import com.lumen.www.json.JsonResult;
import com.lumen.www.vo.AdminUser;

public interface AdminService {

    // 1차 로그인
    JsonResult adminLogin(AdminUser adminUser);

    // 2차 로그인
    AdminUser adminLoginCk(AdminUser adminUser);

    // 가입자 현황
    JsonResult subscriberCount();

    // 메인페이지 월별가입자 그래프
    JsonResult getMonthlySalesChart();

    // 메인페이지 현황지표
    JsonResult getCurrentSituation();

    // 메인페이지 문의현황
    JsonResult getMainInquiryList();

    /*  String loginck(AdminUser adminUser);

      ResponseCookie loginck2(AdminUser adminUser);
  */
}