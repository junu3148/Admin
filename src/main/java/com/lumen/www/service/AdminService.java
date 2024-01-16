package com.lumen.www.service;

import com.lumen.www.dto.JsonResult;
import com.lumen.www.dto.AdminUser;
import com.lumen.www.dto.PromotionsDTO;

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

    // 프로모션 등록
    JsonResult addPromotions(PromotionsDTO promotionsDTO);


    /*  String loginck(AdminUser adminUser);

      ResponseCookie loginck2(AdminUser adminUser);
  */
}