package vikinglab.lumen.dao;

import vikinglab.lumen.vo.AdminUser;
import vikinglab.lumen.vo.CurrentSituationVO;
import vikinglab.lumen.vo.MonthVO;
import vikinglab.lumen.vo.QnaVO;

import java.util.List;

public interface AdminRepository {

    // 1차 로그인
    AdminUser adminLogin(AdminUser adminUser);

    // 2차 로그인
    AdminUser adminLoginCk(AdminUser adminUser);

    // 2차로그인
    AdminUser adminLoginCk(String userName);

    // 가입자 현황
    int subscriberCount();

    // 메인페이지 월별가입자 그래프
    MonthVO getMonthlySalesChart();

    // 메인페이지 현황지표
    List<CurrentSituationVO> getCurrentSituation();

    // 메인페이지 문의현황
    List<QnaVO> getMainInquiryList();


}