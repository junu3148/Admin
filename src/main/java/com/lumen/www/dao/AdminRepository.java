package com.lumen.www.dao;

import com.lumen.www.dto.AdminUser;
import com.lumen.www.dto.InquiryDTO;
import com.lumen.www.dto.PromotionsDTO;
import com.lumen.www.dto.UserActivityDTO;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

public interface AdminRepository {

    /**
     * 주어진 관리자 사용자 정보를 바탕으로 해당 관리자 정보를 조회합니다.
     *
     * @param adminUser 조회할 관리자 사용자 정보
     * @return 조회된 AdminUser 객체 또는 null
     */
    AdminUser getAdminUser(AdminUser adminUser);

    /**
     * 1차 로그인을 위한 메소드입니다.
     *
     * @param adminUser 로그인 시도하는 관리자 사용자 정보
     * @return 로그인 성공 시 AdminUser 객체, 실패 시 null
     */
    AdminUser adminLogin(AdminUser adminUser);

    /**
     * 2차 로그인을 처리합니다.
     *
     * @param adminUser 로그인 시도하는 관리자 사용자 정보
     * @return 로그인 성공 시 AdminUser 객체, 실패 시 null
     */
    AdminUser adminLoginCk(AdminUser adminUser);

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

}
