package com.lumen.www.dao;

import com.lumen.www.dto.*;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AdminRepositoryImpl implements AdminRepository {

    private final SqlSession sqlSession;

    // 아이디
    @Override
    public Optional<AdminUser> findByUsername(String adminId) {
        return Optional.ofNullable(sqlSession.selectOne("admin.findByUsername", adminId));
    }


    // 관리자 권한
    @Override
    public int getRole(String adminId) {
        return sqlSession.selectOne("admin.getRole", adminId);
    }

    // 유저 정보 가져오기
    @Override
    public AdminUser getAdminUser(AdminUser adminUser) {
        return sqlSession.selectOne("admin.getAdminUser", adminUser);
    }

    // 2차 로그인
    @Override
    public Optional<AdminUser> adminLoginCk(AdminUser adminUser) {
        return Optional.ofNullable(sqlSession.selectOne("admin.adminLoginCk", adminUser));
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

    // 문의 리스트
    @Override
    public List<InquiryDTO> getInquiryList() {
        return sqlSession.selectList("admin.getInquiryList");
    }

    // 가입자리스트
    @Override
    public List<JoinListDTO> getJoinList(JoinSearchDTO joinSearchDTO) {
        return sqlSession.selectList("admin.getJoinList", joinSearchDTO);
    }

    // 가입자 세부정보
    @Override
    public UserDTO getUserDetails(UserDTO userDTO) {
        return sqlSession.selectOne("admin.getUserDetails", userDTO);
    }

    // 가입자 강제탈퇴
    @Override
    public int adminJoinUserDelete(UserDTO userDTO) {
        return sqlSession.update("admin.adminJoinUserDelete", userDTO);
    }

    // 탈퇴회원 30일 후 데이터 삭제
    @Override
    public void deleteUsersWithStatusOlderThanOneMonth() {
        sqlSession.delete("admin.deleteUsersWithStatusOlderThanOneMonth");
    }
}
