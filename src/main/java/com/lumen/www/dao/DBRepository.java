package com.lumen.www.dao;

import com.lumen.www.vo.AdminUser;
import com.lumen.www.vo.CurrentSituationVO;
import com.lumen.www.vo.MonthVO;
import com.lumen.www.vo.QnaVO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DBRepository implements AdminRepository{

    private final SqlSession sqlSession;

    @Autowired
    public DBRepository(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }
    @Override
    public AdminUser adminLogin(AdminUser adminUser) {

        return sqlSession.selectOne("AdminMapper.getAdminUser",adminUser);
    }

    @Override
    public AdminUser adminLoginCk(AdminUser adminUser) {
        return null;
    }

    @Override
    public AdminUser adminLoginCk(String userName) {
        return null;
    }

    @Override
    public int subscriberCount() {
        return 0;
    }

    @Override
    public MonthVO getMonthlySalesChart() {
        return null;
    }

    @Override
    public List<CurrentSituationVO> getCurrentSituation() {
        return null;
    }

    @Override
    public List<QnaVO> getMainInquiryList() {
        return null;
    }
}
