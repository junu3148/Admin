package com.lumen.www.dao;

import com.lumen.www.dto.*;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AdminRepositoryImpl implements AdminRepository {

    private final SqlSession sqlSession;
    private static final Logger logger = LoggerFactory.getLogger(AdminRepositoryImpl.class);


     // 아이디
     @Override
     public Optional<AdminUser> findByUsername(String adminId) {
         AdminUser adminUser = sqlSession.selectOne("admin.findByUsername", adminId);
         return Optional.ofNullable(adminUser);
     }


    // 관리자 권한
    @Override
    public int getRole(String adminId){
        return sqlSession.selectOne("admin.getRole", adminId);
    }

    // 유저 정보 가져오기
    @Override
    public AdminUser getAdminUser(AdminUser adminUser) {
        try {
            return sqlSession.selectOne("admin.getAdminUser", adminUser);
        } catch (Exception e) {
            logger.error("Error getting admin user", e);
            throw new DataAccessResourceFailureException("Error getting admin user", e);
        }
    }

    // 1차 로그인
    public AdminUser adminLogin(AdminUser adminUser) {
        try {
            return sqlSession.selectOne("admin.adminLogin", adminUser);
        } catch (Exception e) {
            logger.error("Error in admin login", e);
            throw new DataAccessResourceFailureException("Error in admin login", e);
        }
    }

    // 2차 로그인
    @Override
    public AdminUser adminLoginCk(AdminUser adminUser) {
        try {
            return sqlSession.selectOne("admin.adminLoginCk", adminUser);
        } catch (Exception e) {
            logger.error("Error in admin login check", e);
            throw new DataAccessResourceFailureException("Error in admin login check", e);
        }
    }

    // 가입자 현황
    @Override
    public int subscriberCount() {
        try {
            return sqlSession.selectOne("admin.subscriberCount");
        } catch (Exception e) {
            logger.error("Error getting subscriber count", e);
            throw new DataAccessResourceFailureException("Error getting subscriber count", e);
        }
    }

    // 메인페이지 월별가입자 그래프
    @Override
    public List<Map<String, Object>> getMonthlySubscriber() {
        try {
            return sqlSession.selectList("admin.getMonthlySubscriber");
        } catch (Exception e) {
            logger.error("Error getting monthly subscribers", e);
            throw new DataAccessResourceFailureException("Error getting monthly subscribers", e);
        }
    }

    // 메인페이지 현황지표
    @Override
    public List<UserActivityDTO> getUserActivity() {
        try {
            return sqlSession.selectList("admin.getUserActivity");
        } catch (Exception e) {
            logger.error("Error getting user activity", e);
            throw new DataAccessResourceFailureException("Error getting user activity", e);
        }
    }

    // 문의 리스트
    @Override
    public List<InquiryDTO> getInquiryList() {
        try {
            return sqlSession.selectList("admin.getInquiryList");
        } catch (Exception e) {
            logger.error("Error getting inquiry list", e);
            throw new DataAccessResourceFailureException("Error getting inquiry list", e);
        }
    }

    // 가입자리스트
    @Override
    public List<JoinListDTO> getJoinList(JoinSearchDTO joinSearchDTO) {
        try {

            System.out.println(joinSearchDTO);
            //return null;
            return sqlSession.selectList("admin.getJoinList", joinSearchDTO);
        } catch (Exception e) {
            logger.error("Error getting join list", e);
            throw new DataAccessResourceFailureException("Error getting join list", e);
        }
    }
}
