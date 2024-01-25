package com.lumen.www.service;

import com.lumen.www.dto.*;
import org.springframework.http.ResponseEntity;

public interface AdminService {

    /**
     * 관리자 로그인을 체크합니다.
     *
     * @param adminUser 로그인을 시도하는 관리자의 사용자 정보.
     * @return 로그인 성공 시, 상태 코드와 관련 정보를 포함하는 ResponseEntity 객체를 반환합니다.
     */
    ResponseEntity<?> adminLoginCk(AdminUser adminUser);

    /**
     * 가입자 현황을 반환합니다.
     *
     * @return 가입자 수와 관련된 정보를 포함하는 JsonResult 객체.
     */
    JsonResult subscriberCount();

    /**
     * 메인 페이지에 표시될 월별 가입자 그래프 데이터를 제공합니다.
     *
     * @return 월별 가입자 통계를 포함하는 JsonResult 객체.
     */
    JsonResult getMonthlySalesChart();

    /**
     * 메인 페이지에 표시될 현황지표를 제공합니다.
     *
     * @return 현황지표 데이터를 포함하는 JsonResult 객체.
     */
    JsonResult getCurrentSituation();

    /**
     * 메인 페이지에 표시될 문의 현황을 제공합니다.
     *
     * @return 문의 리스트를 포함하는 JsonResult 객체.
     */
    JsonResult getMainInquiryList();

    /**
     * 조건에 따른 가입자 현황을 반환합니다.
     *
     * @param joinSearchDTO 가입자 조회에 사용되는 검색 조건.
     * @return 조건에 맞는 가입자 목록을 포함하는 JsonResult 객체.
     */
    JsonResult getJoinList(JoinSearchDTO joinSearchDTO);

    /**
     * 새로운 프로모션을 등록합니다.
     *
     * @param promotionsDTO 등록할 프로모션의 데이터 전송 객체.
     * @return 프로모션 등록 결과를 포함하는 JsonResult 객체.
     */
    JsonResult addPromotions(PromotionsDTO promotionsDTO);


}