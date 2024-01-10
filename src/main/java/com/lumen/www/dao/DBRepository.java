package com.lumen.www.dao;

import com.lumen.www.dto.AdminUser;
import com.lumen.www.dto.InquiryDTO;
import com.lumen.www.dto.MonthlySubscriberDTO;
import com.lumen.www.dto.UserActivityDTO;
import com.lumen.www.vo.CurrentSituationVO;
import com.lumen.www.vo.MonthVO;
import com.lumen.www.vo.QnaVO;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class DBRepository implements AdminRepository {

    private final SqlSession sqlSession;

    // 유저 정보 가져오기
    @Override
    public AdminUser getAdminUser(AdminUser adminUser) {
        return sqlSession.selectOne("admin.getAdminUser", adminUser);
    }

    // 1차 로그인

    @Override
    public AdminUser adminLogin(AdminUser adminUser) {
        return sqlSession.selectOne("admin.adminLogin", adminUser);
    }

    // 2차 로그인
    @Override
    public AdminUser adminLoginCk(AdminUser adminUser) {
        return sqlSession.selectOne("admin.adminLoginCk", adminUser);
    }

    // 가입자 현황
    @Override
    public int subscriberCount() {
        return sqlSession.selectOne("admin.subscriberCount");
    }

    // 메인페이지 월별가입자 그래프
    @Override
    public List<Map<String, Object>> getMonthlySubscriber() {
        return sqlSession.selectList("admin.getMonthlySubscriber");
    }

    // 메인페이지 현황지표
    @Override
    public List<UserActivityDTO> getUserActivity() {
        return sqlSession.selectList("admin.getUserActivity");
    }

    @Override
    public List<InquiryDTO> getInquiryList() {
        return sqlSession.selectList("admin.getInquiryList");
    }
}
