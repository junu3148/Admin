package com.lumen.www.dao;

import com.lumen.www.dto.AdminUser;
import com.lumen.www.dto.InquiryDTO;
import com.lumen.www.dto.MonthlySubscriberDTO;
import com.lumen.www.dto.UserActivityDTO;
import com.lumen.www.vo.CurrentSituationVO;
import com.lumen.www.vo.MonthVO;
import com.lumen.www.vo.QnaVO;

import java.util.List;
import java.util.Map;

public interface AdminRepository {

    // AdminUser 정보 가져오기
    AdminUser getAdminUser(AdminUser adminUser);

    // 1차 로그인
    AdminUser adminLogin(AdminUser adminUser);

    // 2차 로그인
    AdminUser adminLoginCk(AdminUser adminUser);

    // 가입자 현황
    int subscriberCount();

    // 메인페이지 월별가입자 그래프
    List<Map<String, Object>> getMonthlySubscriber();

    // 메인페이지 현황지표
    List<UserActivityDTO> getUserActivity();

    // 메인페이지 문의현황
    List<InquiryDTO> getInquiryList();


}