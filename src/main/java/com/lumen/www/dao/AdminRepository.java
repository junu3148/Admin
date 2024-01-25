package com.lumen.www.dao;

import com.lumen.www.dto.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AdminRepository {

    /**
     * 주어진 adminId를 이용하여 관리자 사용자 정보를 조회합니다.
     *
     * 이 메서드는 adminId를 기반으로 데이터베이스에서 AdminUser 객체를 조회합니다.
     * 조회된 관리자 정보가 존재하는 경우 Optional<AdminUser> 형태로 반환되며,
     * 존재하지 않는 경우 Optional.empty()가 반환됩니다.
     *
     * @param adminId 조회할 관리자 사용자의 ID.
     * @return 조회된 AdminUser 객체를 포함하는 Optional 객체.
     */
    Optional<AdminUser> findByUsername(String adminId);

    /**
     * 주어진 adminId에 해당하는 관리자의 역할 정보를 조회합니다.
     *
     * 이 메서드는 adminId를 기반으로 해당 관리자의 역할을 데이터베이스에서 조회합니다.
     * 조회된 역할 정보는 int 형태로 반환됩니다.
     *
     * @param adminId 조회할 관리자 사용자의 ID.
     * @return 조회된 관리자의 역할 정보.
     */
    int getRole(String adminId);


    /**
     * 주어진 관리자 사용자 정보를 바탕으로 해당 관리자 정보를 조회합니다.
     *
     * @param adminUser 조회할 관리자 사용자 정보
     * @return 조회된 AdminUser 객체 또는 null
     */
    AdminUser getAdminUser(AdminUser adminUser);

    /**
     * 2차 로그인을 처리합니다.
     *
     * @param adminUser 로그인 시도하는 관리자 사용자 정보
     * @return 로그인 성공 시 AdminUser 객체, 실패 시 null
     */
    Optional<AdminUser> adminLoginCk(AdminUser adminUser);

    /**
     * 현재 가입자 수를 반환합니다.
     *
     * @return 가입자 수
     */
    int subscriberCount();

    /**
     * 월별 가입자 수를 반환합니다.
     * 반환되는 Map의 구조는 월(month)과 해당 월의 가입자 수(subscribers_count)를 포함합니다.
     *
     * @return 월별 가입자 수 정보를 담은 Map 리스트
     */
    List<Map<String, Object>> getMonthlySubscriber();

    /**
     * 사용자 활동 현황을 조회합니다.
     *
     * @return 사용자 활동 정보를 담은 UserActivityDTO 리스트
     */
    List<UserActivityDTO> getUserActivity();

    /**
     * 최근 문의 사항 목록을 반환합니다.
     *
     * @return 문의 사항 정보를 담은 InquiryDTO 리스트
     */
    List<InquiryDTO> getInquiryList();


    /**
     * 검색 조건별 가입자 리스트를 반환합니다.
     *
     * @return 조건별 가입자 정보를 담은 JoinSearchDTO 리스트
     */
    List<JoinListDTO> getJoinList(JoinSearchDTO joinSearchDTO);


}
