package com.lumen.www.dao;

import com.lumen.www.dto.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AdminRepository {

    /**
     * 주어진 adminId를 이용하여 관리자 사용자 정보를 조회합니다.
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
     * @param adminId 조회할 관리자 사용자 정보
     * @return 조회된 AdminUser 객체 또는 null
     */
    AdminDTO getAdminUser(String adminId);

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
     * 검색 조건에 따라 가입자 리스트를 반환합니다.
     * 이 메서드는 JoinSearchDTO에 정의된 조건을 기반으로 가입자 정보를 검색합니다.
     * 결과는 JoinListDTO 객체의 리스트로 반환됩니다.
     *
     * @param searchDTO 가입자 검색에 사용될 조건을 담은 DTO 객체.
     * @return 조건에 맞는 가입자 정보를 담은 JoinListDTO 리스트.
     */
    List<JoinListDTO> getJoinList(SearchDTO searchDTO);

    /**
     * 특정 사용자의 상세 정보를 반환합니다.
     * 이 메서드는 UserDTO 객체에 포함된 식별 정보를 사용하여 사용자의 상세 정보를 조회합니다.
     * 조회된 정보는 UserDTO 객체로 반환됩니다.
     *
     * @param userDTO 사용자 식별 정보를 포함한 DTO 객체.
     * @return 조회된 사용자의 상세 정보를 담은 UserDTO 객체.
     */
    UserDTO getUserDetails(UserDTO userDTO);

    /**
     * 특정 사용자를 강제로 탈퇴시킵니다.
     * 이 메서드는 UserDTO 객체에 담긴 식별 정보를 사용하여 해당 사용자를 탈퇴 처리합니다.
     * 처리 결과는 영향 받은 레코드 수로 반환됩니다.
     *
     * @param userDTO 강제 탈퇴시킬 사용자의 식별 정보를 담은 DTO 객체.
     * @return 처리된 레코드의 수.
     */
    int adminJoinUserDelete(UserDTO userDTO);

    /**
     * 상태 변경일로부터 한 달이 지난 사용자를 삭제합니다.
     * 이 메서드는 자동으로 한 달 이상 상태가 변경되지 않은 사용자를 삭제합니다.
     * 삭제는 데이터베이스에서 직접 수행되며, 별도의 반환값은 없습니다.
     */
    void deleteUsersWithStatusOlderThanOneMonth();

    /**
     * 가격 목록을 조회합니다.
     * 이 메서드는 PriceSearchDTO 객체를 인자로 받아 해당 조건에 맞는 가격 목록을 조회합니다.
     *
     * @param priceSearchDTO 가격 목록 조회에 사용되는 조건을 포함하는 데이터 전송 객체.
     * @return 조회된 가격 목록을 포함하는 PriceListDTO 객체 리스트.
     */
    List<PriceListDTO> getPriceList(PriceSearchDTO priceSearchDTO);

    /**
     * 사용자의 상태를 업데이트합니다.
     * 이 메서드는 UserDTO 객체를 인자로 받아 사용자의 상태를 업데이트합니다.
     *
     * @param userDTO 사용자 상태 업데이트에 필요한 정보를 포함하는 데이터 전송 객체.
     * @return 업데이트된 행의 수를 나타내는 정수.
     */
    int updateUserStatus(UserDTO userDTO);

    /**
     * 구독 종료 목록을 조회합니다.
     * 이 메서드는 SearchDTO 객체를 인자로 받아 구독이 종료된 목록을 조회합니다.
     *
     * @param searchDTO 구독 종료 목록 조회에 사용되는 조건을 포함하는 데이터 전송 객체.
     * @return 조회된 구독 종료 목록을 포함하는 PayListDTO 객체 리스트.
     */
    List<PayListDTO> getSubscriptionEndList(SearchDTO searchDTO);

    /**
     * 청구서 목록을 조회합니다.
     * 이 메서드는 SearchDTO 객체를 인자로 받아 해당 조건에 맞는 청구서 목록을 조회합니다.
     *
     * @param searchDTO 청구서 목록 조회에 사용되는 조건을 포함하는 데이터 전송 객체.
     * @return 조회된 청구서 목록을 포함하는 InvoiceListDTO 객체 리스트.
     */
    List<InvoiceListDTO> getInvoiceList(SearchDTO searchDTO);

    /**
     * 청구서 세부 사항을 조회합니다.
     * 이 메서드는 InvoiceDTO 객체를 인자로 받아 특정 청구서의 세부 사항을 조회합니다.
     *
     * @param invoiceDTO 청구서 세부 사항 조회에 필요한 정보를 포함하는 데이터 전송 객체.
     * @return 조회된 청구서의 세부 정보를 포함하는 InvoiceDTO 객체.
     */
    InvoiceDTO getInvoiceDetails(InvoiceDTO invoiceDTO);

    /**
     * 1:1 문의 목록을 조회합니다.
     * 이 메서드는 SearchDTO 객체를 인자로 받아 해당 조건에 맞는 1:1 문의 목록을 조회합니다.
     *
     * @param searchDTO 1:1 문의 목록 조회에 사용되는 조건을 포함하는 데이터 전송 객체.
     * @return 조회된 1:1 문의 목록을 포함하는 InquiryDTO 객체 리스트.
     */
    List<InquiryDTO> getInquiryList(SearchDTO searchDTO);

    /**
     * 1:1 문의 세부 사항을 조회합니다.
     * 이 메서드는 InquiryDTO 객체를 인자로 받아 특정 1:1 문의의 세부 사항을 조회합니다.
     *
     * @param inquiryDTO 1:1 문의 세부 사항 조회에 필요한 정보를 포함하는 데이터 전송 객체.
     * @return 조회된 1:1 문의의 세부 정보를 포함하는 InquiryDTO 객체.
     */
    InquiryDTO getInquiryDetails(InquiryDTO inquiryDTO);

    /**
     * 1:1 문의 답변을 등록합니다.
     * 이 메서드는 InquiryDTO 객체를 인자로 받아 1:1 문의에 대한 답변을 등록합니다.
     *
     * @param inquiryDTO 1:1 문의 답변 등록에 필요한 정보를 포함하는 데이터 전송 객체.
     * @return 처리 결과를 나타내는 정수 (성공적으로 처리된 경우 1).
     */
    int insertInquiryAnswer(InquiryDTO inquiryDTO);

    /**
     * 공지사항 목록을 조회합니다.
     * 이 메서드는 SearchDTO 객체를 인자로 받아 해당 조건에 맞는 공지사항 목록을 조회합니다.
     *
     * @param searchDTO 공지사항 목록 조회에 사용되는 조건을 포함하는 데이터 전송 객체.
     * @return 조회된 공지사항 목록을 포함하는 NoticeListDTO 객체 리스트.
     */
    List<NoticeListDTO> getNoticeList(SearchDTO searchDTO);

    /**
     * 공지사항 세부 사항을 조회합니다.
     * 이 메서드는 NoticeDTO 객체를 인자로 받아 특정 공지사항의 세부 사항을 조회합니다.
     *
     * @param noticeDTO 공지사항 세부 사항 조회에 필요한 정보를 포함하는 데이터 전송 객체.
     * @return 조회된 공지사항의 세부 정보를 포함하는 NoticeDTO 객체.
     */
    NoticeDTO getNoticeDetails(NoticeDTO noticeDTO);

    /**
     * 공지사항을 등록합니다.
     * 이 메서드는 NoticeDTO 객체를 인자로 받아 새로운 공지사항을 등록합니다.
     *
     * @param noticeDTO 공지사항 등록에 필요한 데이터를 포함하는 데이터 전송 객체.
     * @return 처리 결과를 나타내는 정수 (성공적으로 처리된 경우 1).
     */
    int insertNotice(NoticeDTO noticeDTO);

    /**
     * 공지사항을 업데이트합니다.
     * 이 메서드는 NoticeDTO 객체를 인자로 받아 기존의 공지사항을 업데이트합니다.
     *
     * @param noticeDTO 공지사항 업데이트에 필요한 정보를 포함하는 데이터 전송 객체.
     * @return 처리 결과를 나타내는 정수 (성공적으로 처리된 경우 1).
     */
    int updateNotice(NoticeDTO noticeDTO);

    /**
     * 공지사항을 삭제합니다.
     * 이 메서드는 NoticeDTO 객체를 인자로 받아 특정 공지사항을 삭제합니다.
     *
     * @param noticeDTO 삭제할 공지사항의 정보를 포함하는 데이터 전송 객체.
     * @return 처리 결과를 나타내는 정수 (성공적으로 처리된 경우 1).
     */
    int deleteNotice(NoticeDTO noticeDTO);


}