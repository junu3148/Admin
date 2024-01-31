package com.lumen.www.service;

import com.lumen.www.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Key;

public interface AdminService {

    /**
     * 관리자 로그인을 체크합니다.
     *
     * @param adminUser 로그인을 시도하는 관리자의 사용자 정보.
     * @return 로그인 성공 시, 상태 코드와 관련 정보를 포함하는 ResponseEntity 객체를 반환합니다.
     */
    ResponseEntity<?> adminLoginCk(AdminUser adminUser);

    /**
     * 관리자의 사용자 정보를 가져옵니다.
     * 이 메서드는 AdminUser 객체에 포함된 식별 정보를 사용하여 관리자의 상세 정보를 조회합니다.
     * 조회된 정보는 JsonResult 객체로 반환되며, 성공적으로 조회된 경우와 조회에 실패한 경우를 구분하여 처리합니다.
     *
     * @param request 조회할 관리자의 사용자 정보.
     * @return 조회된 관리자 정보를 포함하는 JsonResult 객체. 조회 성공 시, 관련 정보와 함께 반환되며, 실패 시 오류 메시지를 포함합니다.
     */
    JsonResult getAdminUser(HttpServletRequest request);

    /**
     * 가입자 현황을 반환합니다.
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
     * @param searchDTO 가입자 조회에 사용되는 검색 조건.
     * @return 조건에 맞는 가입자 목록을 포함하는 JsonResult 객체.
     */
    JsonResult getJoinList(SearchDTO searchDTO);

    /**
     * 사용자의 상세 정보를 조회합니다.
     *
     * @param userDTO 조회할 사용자의 데이터 전송 객체.
     * @return 사용자 상세 정보를 포함하는 JsonResult 객체.
     */
    JsonResult getUserDetails(UserDTO userDTO);

    /**
     * 새로운 프로모션을 등록합니다.
     *
     * @param promotionsDTO 등록할 프로모션의 데이터 전송 객체.
     * @return 프로모션 등록 결과를 포함하는 JsonResult 객체.
     */
    JsonResult addPromotions(PromotionsDTO promotionsDTO);

    /**
     * 가입자의 강제 탈퇴 처리를 수행합니다.
     * 이 메서드는 UserDTO 객체를 받아 해당 사용자를 강제로 탈퇴시킵니다.
     * 탈퇴 처리는 데이터베이스에서 해당 사용자의 정보를 업데이트하거나 삭제하는 과정을 포함할 수 있습니다.
     * 처리 결과는 성공 여부에 따라 다른 HTTP 상태 코드를 포함하는 ResponseEntity 객체로 반환됩니다.
     *
     * @param userDTO 강제 탈퇴시킬 사용자의 데이터 전송 객체.
     * @return 탈퇴 처리 결과를 나타내는 ResponseEntity 객체. 성공 시 HTTP 상태 코드 200과 함께 1을, 실패 시 적절한 오류 코드와 함께 0 또는 오류 메시지를 반환합니다.
     */
    ResponseEntity<?> adminJoinUserDelete(UserDTO userDTO);

    /**
     * 가입자의 비밀번호를 초기화합니다.
     * 이 메서드는 UserDTO 객체를 받아 해당 사용자의 비밀번호를 초기화합니다.
     * 초기화된 비밀번호는 사용자에게 이메일로 전송됩니다. 이 과정에서
     * 사용자의 식별 정보(예: 사용자 ID 또는 이메일)는 UserDTO 객체에서 추출됩니다.
     *
     * @param userDTO 비밀번호를 초기화할 사용자의 데이터 전송 객체.
     * @return 비밀번호 초기화 결과를 포함하는 JsonResult 객체.
     */
    JsonResult adminJoinPWReset(UserDTO userDTO);

    /**
     * 가격 목록을 조회합니다.
     * 이 메서드는 PriceSearchDTO 객체를 인자로 받아 해당 조건에 맞는 가격 목록을 조회합니다.
     *
     * @param priceSearchDTO 가격 목록 조회 조건을 포함하는 데이터 전송 객체.
     * @return 조회된 가격 목록을 포함하는 JsonResult 객체.
     */
    JsonResult getPriceList(PriceSearchDTO priceSearchDTO);

    /**
     * 사용자의 상태를 업데이트합니다.
     * 이 메서드는 UserDTO 객체를 받아 해당 사용자의 상태를 업데이트합니다.
     *
     * @param userDTO 상태를 업데이트할 사용자의 데이터 전송 객체.
     * @return 업데이트 결과를 나타내는 ResponseEntity 객체.
     */
    ResponseEntity<?> updateUserStatus(UserDTO userDTO);

    /**
     * 구독 종료 목록을 조회합니다.
     * 이 메서드는 SearchDTO 객체를 인자로 받아 구독이 종료된 사용자 목록을 조회합니다.
     *
     * @param searchDTO 구독 종료 사용자 목록 조회 조건을 포함하는 데이터 전송 객체.
     * @return 조회된 구독 종료 목록을 포함하는 JsonResult 객체.
     */
    JsonResult getSubscriptionEndList(SearchDTO searchDTO);

    JsonResult getInvoiceList(SearchDTO searchDTO);

    JsonResult getInvoiceDetails(InvoiceDTO invoiceDTO);

    ResponseEntity<?> invoiceEmailShipment (@RequestBody InvoiceDTO invoiceDTO);

    JsonResult getInquiryList (SearchDTO searchDTO);


}