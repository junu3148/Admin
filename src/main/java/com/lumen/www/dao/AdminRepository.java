package com.lumen.www.dao;

import com.lumen.www.dto.user.JoinListDTO;
import com.lumen.www.dto.common.SearchDTO;
import com.lumen.www.dto.faq.FaqDTO;
import com.lumen.www.dto.inquiry.InquiryDTO;
import com.lumen.www.dto.inquiry.InquiryListDTO;
import com.lumen.www.dto.invoice.InvoiceDTO;
import com.lumen.www.dto.invoice.InvoiceListDTO;
import com.lumen.www.dto.notice.NoticeDTO;
import com.lumen.www.dto.notice.NoticeListDTO;
import com.lumen.www.dto.payment.PayListDTO;
import com.lumen.www.dto.pricing.PriceListDTO;
import com.lumen.www.dto.pricing.PriceSearchDTO;
import com.lumen.www.dto.terms.TermsDTO;
import com.lumen.www.dto.user.AdminDTO;
import com.lumen.www.dto.user.AdminUser;
import com.lumen.www.dto.main.UserActivityDTO;
import com.lumen.www.dto.user.UserDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AdminRepository {

    /**
     * 주어진 관리자 ID에 해당하는 관리자 사용자 정보를 조회합니다.
     * <p>
     * 이 메서드는 시스템 내에서 입력받은 관리자 ID와 일치하는 관리자 사용자 정보를
     * 조회합니다. 조회된 관리자 사용자 정보는 {@code AdminUser} 객체로 반환되며,
     * {@code Optional}로 감싸져 있어 정보가 존재하지 않는 경우를 안전하게 처리할 수 있습니다.
     * 이를 통해 null 체크를 명시적으로 수행하지 않고도 존재하지 않는 사용자 정보에 대한
     * 처리를 용이하게 합니다.
     *
     * @param adminId 조회하고자 하는 관리자의 ID를 나타내는 {@code String}.
     *                이 값은 시스템에서 관리자를 유일하게 식별하는 데 사용됩니다.
     * @return 조회된 관리자 사용자 정보를 담고 있는 {@code AdminUser} 객체를 담고 있는 {@code Optional}.
     * 사용자 정보가 존재할 경우 해당 객체를 반환하며, 그렇지 않을 경우 비어 있는 {@code Optional}을 반환합니다.
     */

    Optional<AdminUser> findByUsername(String adminId);

    /**
     * 주어진 관리자 ID에 해당하는 사용자의 역할을 조회합니다.
     * <p>
     * 이 메서드는 시스템에 등록된 관리자의 ID를 기반으로 해당 관리자의 역할을
     * 식별하는 정수 값을 반환합니다. 반환된 값은 시스템 내에서 정의된 역할에
     * 대응되며, 각 역할은 특정 권한을 가지고 있습니다.
     *
     * @param adminId 역할을 조회하고자 하는 관리자의 ID를 나타내는 {@code String}.
     * @return 관리자의 처음 로그인을 식별하는 String 값을 반환합니다.
     */

    AdminUser firstLoginCk(String adminId);


    /**
     * 관리자 로그인을 검증하고, 성공적으로 로그인한 경우 관리자 사용자 정보를 반환합니다.
     * <p>
     * 이 메서드는 입력받은 {@code AdminUser} 객체의 정보를 사용하여 로그인 시도를 검증합니다.
     * 로그인 검증이 성공한 경우, 해당 관리자 사용자의 정보를 담고 있는 {@code AdminUser} 객체를
     * {@code Optional}로 감싸서 반환합니다. 로그인 검증이 실패한 경우, 비어 있는 {@code Optional}을 반환하여
     * 로그인 실패를 나타냅니다.
     *
     * @param adminUser 로그인 검증을 시도할 관리자 사용자 정보를 담고 있는 {@code AdminUser} 객체.
     * @return 로그인이 성공적으로 검증된 경우, 해당 관리자 사용자 정보를 담고 있는 {@code AdminUser} 객체를
     * 담고 있는 {@code Optional}. 로그인 검증이 실패한 경우, 비어 있는 {@code Optional}을 반환합니다.
     */

    Optional<AdminUser> adminLoginCk(AdminUser adminUser);

    /**
     * 관리자의 첫 로그인 시도를 처리하고 결과 상태를 반환합니다.
     * <p>
     * 이 메서드는 {@code AdminUser} 객체를 통해 전달된 관리자의 로그인 정보를 사용하여 첫 로그인 시도를 처리합니다.
     * 처리 과정에는 관리자 계정의 초기 설정 적용, 로그인 시도 기록, 필요한 검증 수행 등이 포함될 수 있습니다.
     * 처리 결과는 정수 형태의 상태 코드로 반환되며, 이 코드는 첫 로그인 시도의 성공, 실패 또는 그 외 상태를 나타냅니다.
     * <p>
     * @param adminUser 첫 로그인을 시도하는 관리자의 정보를 담고 있는 {@code AdminUser} 객체.
     * @return 처리 결과를 나타내는 상태 코드. 일반적으로 0은 성공, 음수는 다양한 실패 상태를 나타냅니다.
     */

    int adminFirstLogin(AdminUser adminUser);

    /**
     * 주어진 관리자 ID에 해당하는 관리자 사용자 정보를 조회하고, {@code AdminDTO} 객체로 반환합니다.
     * <p>
     * 이 메서드는 입력받은 관리자 ID에 해당하는 관리자의 사용자 정보를 조회합니다.
     * 조회된 정보는 {@code AdminDTO} 객체로 포맷되어 반환되며, 이 객체는 관리자의
     * 주요 정보를 담고 있습니다. 조회할 수 없는 경우, 이 메서드는 예외를 발생시킬 수 있습니다.
     *
     * @param adminId 조회하고자 하는 관리자의 ID를 나타내는 {@code String}.
     * @return 조회된 관리자 사용자 정보를 담고 있는 {@code AdminDTO} 객체.
     */

    AdminDTO getAdminUser(String adminId);

    /**
     * 입력받은 {@code AdminUser} 객체의 정보로 관리자 사용자 정보를 업데이트합니다.
     * <p>
     * 이 메서드는 관리자 사용자의 정보를 최신 상태로 업데이트하는 기능을 수행합니다.
     * 업데이트 과정에서는 {@code AdminUser} 객체에 담긴 정보를 사용하여 기존의 사용자 정보를
     * 갱신합니다. 성공적으로 정보를 업데이트한 경우, 변경된 레코드의 수를 정수로 반환합니다.
     *
     * @param adminUser 업데이트할 관리자 사용자 정보를 담고 있는 {@code AdminUser} 객체.
     * @return 업데이트된 레코드의 수를 나타내는 정수 값. 일반적으로 성공적인 업데이트 시 1을 반환합니다.
     */

    int updateAdminUser(AdminUser adminUser);

    /**
     * 시스템에 등록된 구독자의 총 수를 조회합니다.
     * <p>
     * 이 메서드는 시스템에 현재 등록된 구독자의 총 수를 정수로 반환합니다.
     * 구독자 수는 시스템의 사용량 및 인기도를 파악하는 데 유용한 지표로 사용될 수 있습니다.
     *
     * @return 등록된 구독자의 총 수를 나타내는 정수 값.
     */

    int getSubscriberCount();

    /**
     * 최근 몇 개월간의 구독자 목록을 월별로 조회합니다.
     * <p>
     * 이 메서드는 최근 몇 개월 간에 걸쳐 각 월별로 구독한 사용자의 목록을 조회합니다.
     * 반환되는 목록은 {@code Map<String, Object>}의 형태로, 각 월별 구독자 수,
     * 구독 시작 날짜 등의 정보를 포함할 수 있습니다. 이 정보는 시스템의 성장 추세 분석이나
     * 마케팅 전략 수립에 활용될 수 있습니다.
     *
     * @return 각 월별 구독자 목록을 담고 있는 {@code List<Map<String, Object>>} 객체.
     */

    List<Map<String, Object>> getMonthlySubscriber();

    /**
     * 모든 사용자의 활동 목록을 조회합니다.
     * <p>
     * 이 메서드는 시스템에 등록된 모든 사용자의 활동(예: 로그인, 게시글 작성 등) 목록을 조회하여 반환합니다.
     * 반환되는 목록은 {@code UserActivityDTO} 객체의 리스트 형태로, 각 활동의 상세 정보를 포함합니다.
     *
     * @return 사용자 활동 목록을 담고 있는 {@code UserActivityDTO} 객체의 리스트.
     */

    List<UserActivityDTO> getUserActivity();

    /**
     * 사용자들로부터 접수된 모든 문의사항 목록을 조회합니다.
     * <p>
     * 이 메서드는 사용자들이 제출한 문의사항을 목록 형태로 조회하여 반환합니다.
     * 반환되는 목록은 {@code InquiryDTO} 객체의 리스트 형태로, 각 문의사항의 상세 정보를 포함합니다.
     *
     * @return 문의사항 목록을 담고 있는 {@code InquiryDTO} 객체의 리스트.
     */

    List<InquiryDTO> getInquiryList();

    /**
     * 특정 조건에 따라 필터링된 사용자 가입 목록을 조회합니다.
     * <p>
     * 이 메서드는 입력받은 검색 조건({@code SearchDTO})에 따라 사용자 가입 목록을 필터링하여 조회합니다.
     * 반환되는 목록은 {@code JoinListDTO} 객체의 리스트 형태로, 각 가입 정보의 상세를 포함합니다.
     *
     * @param searchDTO 사용자 가입 목록을 필터링하기 위한 검색 조건을 담고 있는 {@code SearchDTO} 객체.
     * @return 필터링된 사용자 가입 목록을 담고 있는 {@code JoinListDTO} 객체의 리스트.
     */

    List<JoinListDTO> getJoinList(SearchDTO searchDTO);

    /**
     * 주어진 사용자의 상세 정보를 조회합니다.
     * <p>
     * 이 메서드는 입력받은 사용자 정보({@code UserDTO})를 기반으로 해당 사용자의 상세 정보를 조회하여 반환합니다.
     * 반환되는 정보는 {@code UserDTO} 객체 형태로, 사용자의 상세 정보를 포함합니다.
     *
     * @param userDTO 조회하고자 하는 사용자의 정보를 담고 있는 {@code UserDTO} 객체.
     * @return 조회된 사용자의 상세 정보를 담고 있는 {@code UserDTO} 객체.
     */

    UserDTO getUserDetails(UserDTO userDTO);

    /**
     * 관리자에 의한 사용자 가입 정보 삭제를 수행합니다.
     * <p>
     * 이 메서드는 입력받은 사용자 정보({@code UserDTO})를 기반으로 해당 사용자의 가입 정보를 시스템에서 삭제합니다.
     * 성공적으로 정보를 삭제한 경우, 삭제된 레코드의 수를 정수로 반환합니다.
     *
     * @param userDTO 삭제할 사용자의 정보를 담고 있는 {@code UserDTO} 객체.
     * @return 삭제된 레코드의 수를 나타내는 정수 값. 일반적으로 성공적인 삭제 시 1을 반환합니다.
     */

    int deleteUser(UserDTO userDTO);

    /**
     * 한 달 이상 오래된 상태를 가진 사용자를 삭제합니다.
     * <p>
     * 이 메서드는 시스템에서 한 달 이상 변경되지 않은 상태를 가진 사용자를
     * 자동으로 식별하여 삭제하는 기능을 수행합니다. 이는 시스템의 데이터를
     * 최신 상태로 유지하기 위해 주기적으로 수행될 수 있습니다.
     */

    void deleteUsersWithStatusOlderThanOneMonth();

    /**
     * 사용자들이 수락한 프로모션 목록을 조회합니다.
     * <p>
     * 이 메서드는 시스템에서 사용자들이 수락한 다양한 프로모션의 목록을
     * 조회하여 반환합니다. 반환되는 목록은 프로모션의 식별자나 이름을
     * 담고 있는 문자열 리스트 형태입니다.
     *
     * @return 수락된 프로모션 목록을 담고 있는 문자열 리스트.
     */

    List<String> getPromotionsAccept();

    /**
     * 특정 조건에 따라 필터링된 가격 목록을 조회합니다.
     * <p>
     * 이 메서드는 입력받은 가격 검색 조건({@code PriceSearchDTO})에 따라
     * 가격 목록을 필터링하여 조회합니다. 반환되는 목록은 {@code PriceListDTO} 객체의
     * 리스트 형태로, 각 가격 정보의 상세를 포함합니다.
     *
     * @param priceSearchDTO 가격 목록을 필터링하기 위한 검색 조건을 담고 있는 {@code PriceSearchDTO} 객체.
     * @return 필터링된 가격 목록을 담고 있는 {@code PriceListDTO} 객체의 리스트.
     */

    List<PriceListDTO> getPriceList(PriceSearchDTO priceSearchDTO);

    /**
     * 주어진 사용자의 상태를 업데이트합니다.
     * <p>
     * 이 메서드는 입력받은 사용자 정보({@code UserDTO})에 기반하여
     * 해당 사용자의 상태를 업데이트하는 기능을 수행합니다. 성공적으로 상태를
     * 업데이트한 경우, 변경된 레코드의 수를 정수로 반환합니다.
     *
     * @param userDTO 업데이트할 사용자의 정보를 담고 있는 {@code UserDTO} 객체.
     * @return 업데이트된 레코드의 수를 나타내는 정수 값. 일반적으로 성공적인 업데이트 시 1을 반환합니다.
     */

    int updateUserStatus(UserDTO userDTO);

    /**
     * 구독 종료가 임박한 사용자 목록을 조회합니다.
     * <p>
     * 이 메서드는 입력받은 검색 조건({@code SearchDTO})에 따라
     * 구독 종료가 임박한 사용자의 목록을 필터링하여 조회합니다. 반환되는 목록은
     * {@code PayListDTO} 객체의 리스트 형태로, 각 사용자의 구독 정보의 상세를 포함합니다.
     *
     * @param searchDTO 사용자 목록을 필터링하기 위한 검색 조건을 담고 있는 {@code SearchDTO} 객체.
     * @return 필터링된 구독 종료가 임박한 사용자 목록을 담고 있는 {@code PayListDTO} 객체의 리스트.
     */

    List<PayListDTO> getSubscriptionEndList(SearchDTO searchDTO);

    /**
     * 특정 조건에 따라 필터링된 송장 목록을 조회합니다.
     * <p>
     * 이 메서드는 입력받은 검색 조건({@code SearchDTO})에 따라 송장 목록을
     * 필터링하여 조회합니다. 반환되는 목록은 {@code InvoiceListDTO} 객체의 리스트 형태로,
     * 각 송장의 상세 정보를 포함합니다.
     *
     * @param searchDTO 송장 목록을 필터링하기 위한 검색 조건을 담고 있는 {@code SearchDTO} 객체.
     * @return 필터링된 송장 목록을 담고 있는 {@code InvoiceListDTO} 객체의 리스트.
     */

    List<InvoiceListDTO> getInvoiceList(SearchDTO searchDTO);

    /**
     * 주어진 송장의 상세 정보를 조회합니다.
     * <p>
     * 이 메서드는 입력받은 송장 정보({@code InvoiceDTO})를 기반으로 해당 송장의
     * 상세 정보를 조회하여 반환합니다. 반환되는 정보는 {@code InvoiceDTO} 객체 형태로,
     * 송장의 상세 정보를 포함합니다.
     *
     * @param invoiceDTO 조회하고자 하는 송장의 정보를 담고 있는 {@code InvoiceDTO} 객체.
     * @return 조회된 송장의 상세 정보를 담고 있는 {@code InvoiceDTO} 객체.
     */

    InvoiceDTO getInvoiceDetails(InvoiceDTO invoiceDTO);

    /**
     * 특정 조건에 따라 필터링된 문의 목록을 조회합니다.
     * <p>
     * 이 메서드는 입력받은 검색 조건({@code SearchDTO})에 따라 문의 목록을
     * 필터링하여 조회합니다. 반환되는 목록은 {@code InquiryListDTO} 객체의 리스트 형태로,
     * 각 문의의 상세 정보를 포함합니다.
     *
     * @param searchDTO 문의 목록을 필터링하기 위한 검색 조건을 담고 있는 {@code SearchDTO} 객체.
     * @return 필터링된 문의 목록을 담고 있는 {@code InquiryListDTO} 객체의 리스트.
     */

    List<InquiryListDTO> getInquiryList(SearchDTO searchDTO);

    /**
     * 주어진 문의의 상세 정보를 조회합니다.
     * <p>
     * 이 메서드는 입력받은 문의 정보({@code InquiryDTO})를 기반으로 해당 문의의
     * 상세 정보를 조회하여 반환합니다. 반환되는 정보는 {@code InquiryDTO} 객체 형태로,
     * 문의의 상세 정보를 포함합니다.
     *
     * @param inquiryDTO 조회하고자 하는 문의의 정보를 담고 있는 {@code InquiryDTO} 객체.
     * @return 조회된 문의의 상세 정보를 담고 있는 {@code InquiryDTO} 객체.
     */

    InquiryDTO getInquiryDetails(InquiryDTO inquiryDTO);

    /**
     * 문의에 대한 답변을 시스템에 삽입합니다.
     * <p>
     * 이 메서드는 입력받은 문의 정보({@code InquiryDTO})에 답변을 추가하여 시스템에 저장하는
     * 기능을 수행합니다. 답변이 성공적으로 삽입된 경우, 삽입된 레코드의 수를 정수로 반환합니다.
     *
     * @param inquiryDTO 답변을 추가할 문의의 정보를 담고 있는 {@code InquiryDTO} 객체.
     * @return 삽입된 레코드의 수를 나타내는 정수 값. 일반적으로 성공적인 삽입 시 1을 반환합니다.
     */

    int insertInquiryAnswer(InquiryDTO inquiryDTO);

    /**
     * 특정 조건에 따라 필터링된 공지 목록을 조회합니다.
     * <p>
     * 이 메서드는 입력받은 검색 조건({@code SearchDTO})에 따라 공지 목록을
     * 필터링하여 조회합니다. 반환되는 목록은 {@code NoticeListDTO} 객체의 리스트 형태로,
     * 각 공지의 상세 정보를 포함합니다.
     *
     * @param searchDTO 공지 목록을 필터링하기 위한 검색 조건을 담고 있는 {@code SearchDTO} 객체.
     * @return 필터링된 공지 목록을 담고 있는 {@code NoticeListDTO} 객체의 리스트.
     */

    List<NoticeListDTO> getNoticeList(SearchDTO searchDTO);

    /**
     * 주어진 공지의 상세 정보를 조회합니다.
     * <p>
     * 이 메서드는 입력받은 공지 정보({@code NoticeDTO})를 기반으로 해당 공지의
     * 상세 정보를 조회하여 반환합니다. 반환되는 정보는 {@code NoticeDTO} 객체 형태로,
     * 공지의 상세 정보를 포함합니다.
     *
     * @param noticeDTO 조회하고자 하는 공지의 정보를 담고 있는 {@code NoticeDTO} 객체.
     * @return 조회된 공지의 상세 정보를 담고 있는 {@code NoticeDTO} 객체.
     */

    NoticeDTO getNoticeDetails(NoticeDTO noticeDTO);

    /**
     * 새로운 공지를 시스템에 삽입합니다.
     * <p>
     * 이 메서드는 입력받은 공지 정보({@code NoticeDTO})를 사용하여 시스템에 새로운 공지를
     * 삽입하는 기능을 수행합니다. 삽입이 성공적으로 이루어진 경우, 삽입된 레코드의 수를 정수로 반환합니다.
     *
     * @param noticeDTO 삽입할 새로운 공지의 정보를 담고 있는 {@code NoticeDTO} 객체.
     * @return 삽입된 레코드의 수를 나타내는 정수 값. 일반적으로 성공적인 삽입 시 1을 반환합니다.
     */

    int insertNotice(NoticeDTO noticeDTO);

    /**
     * 기존의 공지 정보를 업데이트합니다.
     * <p>
     * 이 메서드는 입력받은 공지 정보({@code NoticeDTO})에 따라 기존의 공지 정보를
     * 업데이트하는 기능을 수행합니다. 업데이트가 성공적으로 이루어진 경우, 변경된 레코드의 수를 정수로 반환합니다.
     *
     * @param noticeDTO 업데이트할 공지의 정보를 담고 있는 {@code NoticeDTO} 객체.
     * @return 업데이트된 레코드의 수를 나타내는 정수 값. 일반적으로 성공적인 업데이트 시 1을 반환합니다.
     */

    int updateNotice(NoticeDTO noticeDTO);

    /**
     * 특정 공지를 시스템에서 삭제합니다.
     * <p>
     * 이 메서드는 입력받은 공지 정보({@code NoticeDTO})를 기반으로 해당 공지를
     * 시스템에서 삭제하는 기능을 수행합니다. 삭제가 성공적으로 이루어진 경우, 삭제된 레코드의 수를 정수로 반환합니다.
     *
     * @param noticeDTO 삭제할 공지의 정보를 담고 있는 {@code NoticeDTO} 객체.
     * @return 삭제된 레코드의 수를 나타내는 정수 값. 일반적으로 성공적인 삭제 시 1을 반환합니다.
     */

    int deleteNotice(NoticeDTO noticeDTO);

    /**
     * 특정 조건에 따라 필터링된 FAQ 목록을 조회합니다.
     * <p>
     * 이 메서드는 입력받은 검색 조건({@code SearchDTO})에 따라 FAQ 목록을
     * 필터링하여 조회합니다. 반환되는 목록은 {@code FaqDTO} 객체의 리스트 형태로,
     * 각 FAQ의 상세 정보를 포함합니다.
     *
     * @param searchDTO FAQ 목록을 필터링하기 위한 검색 조건을 담고 있는 {@code SearchDTO} 객체.
     * @return 필터링된 FAQ 목록을 담고 있는 {@code FaqDTO} 객체의 리스트.
     */

    List<FaqDTO> getFaqList(SearchDTO searchDTO);

    /**
     * 주어진 FAQ의 상세 정보를 조회합니다.
     * <p>
     * 이 메서드는 입력받은 FAQ 정보({@code FaqDTO})를 기반으로 해당 FAQ의
     * 상세 정보를 조회하여 반환합니다. 반환되는 정보는 {@code FaqDTO} 객체 형태로,
     * FAQ의 상세 정보를 포함합니다.
     *
     * @param faqDTO 조회하고자 하는 FAQ의 정보를 담고 있는 {@code FaqDTO} 객체.
     * @return 조회된 FAQ의 상세 정보를 담고 있는 {@code FaqDTO} 객체.
     */

    FaqDTO getFaqDetails(FaqDTO faqDTO);

    /**
     * 새로운 FAQ를 시스템에 삽입합니다.
     * <p>
     * 이 메서드는 입력받은 FAQ 정보({@code FaqDTO})를 사용하여 시스템에 새로운 FAQ를
     * 삽입하는 기능을 수행합니다. 삽입이 성공적으로 이루어진 경우, 삽입된 레코드의 수를 정수로 반환합니다.
     *
     * @param faqDTO 삽입할 새로운 FAQ의 정보를 담고 있는 {@code FaqDTO} 객체.
     * @return 삽입된 레코드의 수를 나타내는 정수 값. 일반적으로 성공적인 삽입 시 1을 반환합니다.
     */

    int insertFaq(FaqDTO faqDTO);

    /**
     * 기존의 FAQ 정보를 업데이트합니다.
     * <p>
     * 이 메서드는 입력받은 FAQ 정보({@code FaqDTO})에 따라 기존의 FAQ 정보를
     * 업데이트하는 기능을 수행합니다. 업데이트가 성공적으로 이루어진 경우, 변경된 레코드의 수를 정수로 반환합니다.
     *
     * @param faqDTO 업데이트할 FAQ의 정보를 담고 있는 {@code FaqDTO} 객체.
     * @return 업데이트된 레코드의 수를 나타내는 정수 값. 일반적으로 성공적인 업데이트 시 1을 반환합니다.
     */

    int updateFaq(FaqDTO faqDTO);

    /**
     * 특정 FAQ를 시스템에서 삭제합니다.
     * <p>
     * 이 메서드는 입력받은 FAQ 정보({@code FaqDTO})를 기반으로 해당 FAQ를
     * 시스템에서 삭제하는 기능을 수행합니다. 삭제가 성공적으로 이루어진 경우, 삭제된 레코드의 수를 정수로 반환합니다.
     *
     * @param faqDTO 삭제할 FAQ의 정보를 담고 있는 {@code FaqDTO} 객체.
     * @return 삭제된 레코드의 수를 나타내는 정수 값. 일반적으로 성공적인 삭제 시 1을 반환합니다.
     */

    int deleteFaq(FaqDTO faqDTO);

    /**
     * 현재 적용되고 있는 이용 약관을 조회합니다.
     * <p>
     * 이 메서드는 시스템에 저장되어 있는 이용 약관의 내용을 조회하여 반환합니다.
     * 반환되는 정보는 {@code TermsDTO} 객체 형태로, 현재 적용되고 있는 이용 약관의
     * 상세 내용을 포함합니다.
     *
     * @return 현재 적용되고 있는 이용 약관의 내용을 담고 있는 {@code TermsDTO} 객체.
     */

    TermsDTO getTermsDetails();

    /**
     * 이용 약관의 내용을 업데이트합니다.
     * <p>
     * 이 메서드는 입력받은 이용 약관 정보({@code TermsDTO})를 사용하여 현재 적용되고 있는
     * 이용 약관을 업데이트하는 기능을 수행합니다. 업데이트가 성공적으로 이루어진 경우, 변경된 레코드의 수를 정수로 반환합니다.
     *
     * @param termsDTO 업데이트할 이용 약관의 정보를 담고 있는 {@code TermsDTO} 객체.
     * @return 업데이트된 레코드의 수를 나타내는 정수 값. 일반적으로 성공적인 업데이트 시 1을 반환합니다.
     */

    int updateTerms(TermsDTO termsDTO);


}