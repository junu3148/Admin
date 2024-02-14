package com.lumen.www.service;

import com.lumen.www.dto.common.JsonResult;
import com.lumen.www.dto.common.SearchDTO;
import com.lumen.www.dto.faq.FaqDTO;
import com.lumen.www.dto.inquiry.InquiryDTO;
import com.lumen.www.dto.invoice.InvoiceDTO;
import com.lumen.www.dto.notice.NoticeDTO;
import com.lumen.www.dto.pricing.PriceSearchDTO;
import com.lumen.www.dto.terms.TermsDTO;
import com.lumen.www.dto.user.AdminUser;
import com.lumen.www.dto.user.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface AdminService {

    /**
     * 관리자 로그인 검증 메서드입니다.
     * <p>
     * 입력받은 {@code AdminUser} 객체의 정보를 사용하여 관리자 로그인을 검증합니다.
     * 검증이 성공하면, 성공 메시지를 담은 {@code ResponseEntity<String>} 객체를 반환합니다.
     * 검증에 실패하면, 실패 메시지를 담은 {@code ResponseEntity<String>} 객체를 반환합니다.
     *
     * @param adminUser 관리자 로그인 정보를 담고 있는 {@code AdminUser} 객체.
     * @return 로그인 검증 결과를 담은 {@code ResponseEntity<String>} 객체.
     */

    ResponseEntity<String> adminLoginCk(AdminUser adminUser);

    /**
     * 현재 세션에서 관리자 사용자 정보를 조회하는 메서드입니다.
     * <p>
     * HTTP 요청에서 관리자 사용자의 정보를 조회하여 {@code JsonResult} 형태로 반환합니다.
     * 관리자 정보가 존재하면, 해당 정보와 함께 성공 결과를 반환합니다.
     * 정보 조회에 실패하면, 오류 메시지를 포함한 결과를 반환합니다.
     *
     * @param request 현재 HTTP 요청 정보를 담고 있는 {@code HttpServletRequest} 객체.
     * @return 조회된 관리자 사용자 정보를 담은 {@code JsonResult} 객체.
     */

    JsonResult getAdminUser(HttpServletRequest request);

    /**
     * 관리자 사용자 정보를 업데이트하는 메서드입니다.
     * <p>
     * 입력받은 {@code AdminUser} 객체의 정보를 사용하여 관리자 사용자 정보를 업데이트합니다.
     * 업데이트가 성공적으로 완료되면, 성공 메시지를 담은 {@code ResponseEntity<String>} 객체를 반환합니다.
     * 업데이트에 실패하면, 실패 메시지를 담은 {@code ResponseEntity<String>} 객체를 반환합니다.
     *
     * @param adminUser 업데이트할 관리자 사용자 정보를 담고 있는 {@code AdminUser} 객체.
     * @return 업데이트 결과를 담은 {@code ResponseEntity<String>} 객체.
     */

    ResponseEntity<String> updateAdminUser(AdminUser adminUser);

    /**
     * 사용자의 로그아웃 처리를 수행합니다.
     * <p>
     * 이 메서드는 HTTP 요청과 관련된 현재 세션에서 사용자의 로그인 정보를 삭제하여 로그아웃을 처리합니다.
     * 로그아웃 처리 후 별도의 반환값은 없으며, 세션에서 사용자 정보가 성공적으로 제거되었다는 것을 의미합니다.
     *
     * @param request 현재 HTTP 요청 정보를 담고 있는 {@code HttpServletRequest} 객체.
     */

    void logout(HttpServletRequest request);

    /**
     * 시스템의 총 구독자 수를 조회합니다.
     * <p>
     * 이 메서드는 시스템에 등록된 총 구독자 수를 조회하고, 그 결과를 {@code JsonResult} 형태로 반환합니다.
     * 반환되는 {@code JsonResult} 객체에는 구독자 수에 관한 정보가 포함되어 있습니다.
     *
     * @return 총 구독자 수를 담고 있는 {@code JsonResult} 객체.
     */

    JsonResult subscriberCount();

    /**
     * 월별 판매 차트 데이터를 조회합니다.
     * <p>
     * 이 메서드는 시스템의 월별 판매 데이터를 조회하고, 그 결과를 시각화하기 위한 차트 데이터 형태로
     * {@code JsonResult} 객체에 담아 반환합니다.
     *
     * @return 월별 판매 차트 데이터를 담고 있는 {@code JsonResult} 객체.
     */

    JsonResult getMonthlySalesChart();

    /**
     * 시스템의 현재 상황을 요약하여 조회합니다.
     * <p>
     * 이 메서드는 시스템의 현재 상황, 예를 들어 사용자 수, 구독자 수, 최근 문의사항 등의 요약 정보를 조회하고,
     * {@code JsonResult} 객체로 반환합니다. 반환되는 객체에는 시스템의 키 메트릭스와 관련된 정보가 포함됩니다.
     *
     * @return 시스템의 현재 상황 요약 정보를 담고 있는 {@code JsonResult} 객체.
     */

    JsonResult getCurrentSituation();

    /**
     * 주요 문의사항 목록을 조회합니다.
     * <p>
     * 이 메서드는 사용자로부터 받은 주요 문의사항들을 조회하고, 그 결과를 {@code JsonResult} 형태로 반환합니다.
     * 반환되는 {@code JsonResult} 객체에는 문의사항 목록에 관한 정보가 포함되어 있습니다.
     *
     * @return 주요 문의사항 목록을 담고 있는 {@code JsonResult} 객체.
     */

    JsonResult getMainInquiryList();

    /**
     * 특정 검색 조건에 맞는 사용자 가입 목록을 조회합니다.
     * <p>
     * 이 메서드는 입력받은 검색 조건({@code SearchDTO})에 따라 필터링된 사용자 가입 목록을 조회하고,
     * 그 결과를 {@code JsonResult} 형태로 반환합니다. 반환되는 {@code JsonResult} 객체에는
     * 가입 목록에 관한 정보가 포함되어 있습니다.
     *
     * @param searchDTO 사용자 가입 목록을 필터링하기 위한 검색 조건을 담고 있는 {@code SearchDTO} 객체.
     * @return 필터링된 사용자 가입 목록을 담고 있는 {@code JsonResult} 객체.
     */

    JsonResult getJoinList(SearchDTO searchDTO);

    /**
     * 특정 사용자의 상세 정보를 조회합니다.
     * <p>
     * 이 메서드는 입력받은 사용자 식별 정보({@code UserDTO})를 바탕으로 특정 사용자의 상세 정보를 조회하고,
     * 그 결과를 {@code JsonResult} 형태로 반환합니다. 반환되는 {@code JsonResult} 객체에는
     * 사용자의 상세 정보가 포함되어 있습니다.
     *
     * @param userDTO 사용자 상세 정보를 조회하기 위한 사용자 식별 정보를 담고 있는 {@code UserDTO} 객체.
     * @return 조회된 사용자의 상세 정보를 담고 있는 {@code JsonResult} 객체.
     */

    JsonResult getUserDetails(UserDTO userDTO);

    /**
     * 관리자에 의한 사용자 가입 삭제 처리를 합니다.
     * <p>
     * 이 메서드는 관리자가 입력한 사용자 식별 정보({@code UserDTO})를 사용하여 해당 사용자의 가입을 삭제합니다.
     * 성공적으로 삭제되면, 성공 메시지를 담은 {@code ResponseEntity<String>}을 반환합니다.
     *
     * @param userDTO 삭제할 사용자의 식별 정보를 담고 있는 {@code UserDTO} 객체.
     * @return 처리 결과를 담고 있는 {@code ResponseEntity<String>} 객체.
     */

    ResponseEntity<String> adminJoinUserDelete(UserDTO userDTO);

    /**
     * 특정 조건에 따른 가격 목록을 조회합니다.
     * <p>
     * 이 메서드는 입력받은 가격 검색 조건({@code PriceSearchDTO})에 따라 필터링된 가격 목록을 조회하고,
     * 그 결과를 {@code JsonResult} 형태로 반환합니다. 반환되는 {@code JsonResult} 객체에는
     * 가격 목록에 관한 정보가 포함되어 있습니다.
     *
     * @param priceSearchDTO 가격 목록을 필터링하기 위한 검색 조건을 담고 있는 {@code PriceSearchDTO} 객체.
     * @return 필터링된 가격 목록을 담고 있는 {@code JsonResult} 객체.
     */

    JsonResult getPriceList(PriceSearchDTO priceSearchDTO);

    /**
     * 사용자 상태를 업데이트 합니다.
     * <p>
     * 이 메서드는 입력받은 사용자 식별 및 상태 정보({@code UserDTO})를 사용하여 사용자의 상태를 업데이트합니다.
     * 성공적으로 업데이트되면, 성공 메시지를 담은 {@code ResponseEntity<String>}을 반환합니다.
     *
     * @param userDTO 업데이트할 사용자의 식별 및 상태 정보를 담고 있는 {@code UserDTO} 객체.
     * @return 처리 결과를 담고 있는 {@code ResponseEntity<String>} 객체.
     */

    ResponseEntity<String> updateUserStatus(UserDTO userDTO);

    /**
     * 구독 종료 목록을 조회합니다.
     * <p>
     * 이 메서드는 입력받은 검색 조건({@code SearchDTO})에 따라 구독이 종료된 사용자 목록을 조회하고,
     * 그 결과를 {@code JsonResult} 형태로 반환합니다. 반환되는 {@code JsonResult} 객체에는
     * 구독 종료 사용자 목록에 관한 정보가 포함되어 있습니다.
     *
     * @param searchDTO 구독 종료 목록을 필터링하기 위한 검색 조건을 담고 있는 {@code SearchDTO} 객체.
     * @return 필터링된 구독 종료 목록을 담고 있는 {@code JsonResult} 객체.
     */

    JsonResult getSubscriptionEndList(SearchDTO searchDTO);

    /**
     * 청구서 목록을 조회합니다.
     * <p>
     * 이 메서드는 입력받은 검색 조건({@code SearchDTO})에 따라 필터링된 청구서 목록을 조회하고,
     * 그 결과를 {@code JsonResult} 형태로 반환합니다. 반환되는 {@code JsonResult} 객체에는
     * 청구서 목록에 관한 정보가 포함되어 있습니다.
     *
     * @param searchDTO 청구서 목록을 필터링하기 위한 검색 조건을 담고 있는 {@code SearchDTO} 객체.
     * @return 필터링된 청구서 목록을 담고 있는 {@code JsonResult} 객체.
     */

    JsonResult getInvoiceList(SearchDTO searchDTO);

    /**
     * 특정 청구서의 상세 정보를 조회합니다.
     * <p>
     * 이 메서드는 입력받은 청구서 식별 정보({@code InvoiceDTO})를 바탕으로 특정 청구서의 상세 정보를 조회하고,
     * 그 결과를 {@code JsonResult} 형태로 반환합니다. 반환되는 {@code JsonResult} 객체에는
     * 청구서의 상세 정보가 포함되어 있습니다.
     *
     * @param invoiceDTO 청구서 상세 정보를 조회하기 위한 청구서 식별 정보를 담고 있는 {@code InvoiceDTO} 객체.
     * @return 조회된 청구서의 상세 정보를 담고 있는 {@code JsonResult} 객체.
     */

    JsonResult getInvoiceDetails(InvoiceDTO invoiceDTO);

    /**
     * 문의 목록을 조회합니다.
     * <p>
     * 이 메서드는 입력받은 검색 조건({@code SearchDTO})에 따라 필터링된 문의 목록을 조회하고,
     * 그 결과를 {@code JsonResult} 형태로 반환합니다. 반환되는 {@code JsonResult} 객체에는
     * 문의 목록에 관한 정보가 포함되어 있습니다.
     *
     * @param searchDTO 문의 목록을 필터링하기 위한 검색 조건을 담고 있는 {@code SearchDTO} 객체.
     * @return 필터링된 문의 목록을 담고 있는 {@code JsonResult} 객체.
     */

    JsonResult getInquiryList(SearchDTO searchDTO);

    /**
     * 특정 문의의 상세 정보를 조회합니다.
     * <p>
     * 이 메서드는 입력받은 문의 식별 정보({@code InquiryDTO})를 바탕으로 특정 문의의 상세 정보를 조회하고,
     * 그 결과를 {@code JsonResult} 형태로 반환합니다. 반환되는 {@code JsonResult} 객체에는
     * 문의의 상세 정보가 포함되어 있습니다.
     *
     * @param inquiryDTO 문의 상세 정보를 조회하기 위한 문의 식별 정보를 담고 있는 {@code InquiryDTO} 객체.
     * @return 조회된 문의의 상세 정보를 담고 있는 {@code JsonResult} 객체.
     */

    JsonResult getInquiryDetails(InquiryDTO inquiryDTO);

    /**
     * 문의에 대한 답변을 등록합니다.
     * <p>
     * 이 메서드는 사용자가 제출한 문의에 대한 답변을 입력받아 시스템에 등록합니다.
     * 등록이 성공적으로 완료되면, 성공 메시지를 담은 {@code ResponseEntity<String>}을 반환합니다.
     *
     * @param inquiryDTO 답변을 등록할 문의의 식별 정보를 담고 있는 {@code InquiryDTO} 객체.
     * @return 처리 결과를 담고 있는 {@code ResponseEntity<String>} 객체.
     */

    ResponseEntity<String> insertInquiryAnswer(InquiryDTO inquiryDTO);

    /**
     * 공지사항 목록을 조회합니다.
     * <p>
     * 이 메서드는 입력받은 검색 조건({@code SearchDTO})에 따라 필터링된 공지사항 목록을 조회하고,
     * 그 결과를 {@code JsonResult} 형태로 반환합니다. 반환되는 {@code JsonResult} 객체에는
     * 공지사항 목록에 관한 정보가 포함되어 있습니다.
     *
     * @param searchDTO 공지사항 목록을 필터링하기 위한 검색 조건을 담고 있는 {@code SearchDTO} 객체.
     * @return 필터링된 공지사항 목록을 담고 있는 {@code JsonResult} 객체.
     */

    JsonResult getNoticeList(SearchDTO searchDTO);

    /**
     * 특정 공지사항의 상세 정보를 조회합니다.
     * <p>
     * 이 메서드는 입력받은 공지사항 식별 정보({@code NoticeDTO})를 바탕으로 특정 공지사항의 상세 정보를 조회하고,
     * 그 결과를 {@code JsonResult} 형태로 반환합니다. 반환되는 {@code JsonResult} 객체에는
     * 공지사항의 상세 정보가 포함되어 있습니다.
     *
     * @param noticeDTO 공지사항 상세 정보를 조회하기 위한 공지사항 식별 정보를 담고 있는 {@code NoticeDTO} 객체.
     * @return 조회된 공지사항의 상세 정보를 담고 있는 {@code JsonResult} 객체.
     */

    JsonResult getNoticeDetails(NoticeDTO noticeDTO);

    /**
     * 새로운 공지사항을 등록합니다.
     * <p>
     * 이 메서드는 사용자의 요청({@code HttpServletRequest})과 공지사항 정보({@code NoticeDTO})를 받아
     * 새로운 공지사항을 등록합니다. 성공적으로 등록되면, 성공 메시지를 담은 {@code ResponseEntity<String>}을 반환합니다.
     *
     * @param request 사용자의 요청 정보를 담고 있는 {@code HttpServletRequest} 객체.
     * @param noticeDTO 등록할 공지사항의 정보를 담고 있는 {@code NoticeDTO} 객체.
     * @return 처리 결과를 담고 있는 {@code ResponseEntity<String>} 객체.
     */

    ResponseEntity<String> insertNotice(HttpServletRequest request, NoticeDTO noticeDTO);

    /**
     * 공지사항 정보를 업데이트 합니다.
     * <p>
     * 이 메서드는 공지사항 정보({@code NoticeDTO})를 받아 해당 공지사항의 정보를 업데이트합니다.
     * 성공적으로 업데이트되면, 성공 메시지를 담은 {@code ResponseEntity<String>}을 반환합니다.
     *
     * @param noticeDTO 업데이트할 공지사항의 정보를 담고 있는 {@code NoticeDTO} 객체.
     * @return 처리 결과를 담고 있는 {@code ResponseEntity<String>} 객체.
     */

    ResponseEntity<String> updateNotice(NoticeDTO noticeDTO);

    /**
     * 특정 공지사항을 삭제합니다.
     * <p>
     * 이 메서드는 공지사항 정보({@code NoticeDTO})를 받아 해당 공지사항을 삭제합니다.
     * 성공적으로 삭제되면, 성공 메시지를 담은 {@code ResponseEntity<String>}을 반환합니다.
     *
     * @param noticeDTO 삭제할 공지사항의 정보를 담고 있는 {@code NoticeDTO} 객체.
     * @return 처리 결과를 담고 있는 {@code ResponseEntity<String>} 객체.
     */

    ResponseEntity<String> deleteNotice(NoticeDTO noticeDTO);

    /**
     * 자주 묻는 질문(FAQ) 목록을 조회합니다.
     * <p>
     * 이 메서드는 입력받은 검색 조건({@code SearchDTO})에 따라 필터링된 FAQ 목록을 조회하고,
     * 그 결과를 {@code JsonResult} 형태로 반환합니다. 반환되는 {@code JsonResult} 객체에는
     * FAQ 목록에 관한 정보가 포함되어 있습니다.
     *
     * @param searchDTO FAQ 목록을 필터링하기 위한 검색 조건을 담고 있는 {@code SearchDTO} 객체.
     * @return 필터링된 FAQ 목록을 담고 있는 {@code JsonResult} 객체.
     */

    JsonResult getFaqList(SearchDTO searchDTO);

    /**
     * 특정 FAQ의 상세 정보를 조회합니다.
     * <p>
     * 이 메서드는 입력받은 FAQ 식별 정보({@code FaqDTO})를 바탕으로 특정 FAQ의 상세 정보를 조회하고,
     * 그 결과를 {@code JsonResult} 형태로 반환합니다. 반환되는 {@code JsonResult} 객체에는
     * FAQ의 상세 정보가 포함되어 있습니다.
     *
     * @param faqDTO FAQ 상세 정보를 조회하기 위한 FAQ 식별 정보를 담고 있는 {@code FaqDTO} 객체.
     * @return 조회된 FAQ의 상세 정보를 담고 있는 {@code JsonResult} 객체.
     */

    JsonResult getFaq(FaqDTO faqDTO);

    /**
     * 새로운 FAQ를 등록합니다.
     * <p>
     * 이 메서드는 사용자의 요청({@code HttpServletRequest})과 FAQ 정보({@code FaqDTO})를 받아
     * 새로운 FAQ를 등록합니다. 성공적으로 등록되면, 성공 메시지를 담은 {@code ResponseEntity<String>}을 반환합니다.
     *
     * @param request 사용자의 요청 정보를 담고 있는 {@code HttpServletRequest} 객체.
     * @param faqDTO 등록할 FAQ의 정보를 담고 있는 {@code FaqDTO} 객체.
     * @return 처리 결과를 담고 있는 {@code ResponseEntity<String>} 객체.
     */

    ResponseEntity<String> insertFaq(HttpServletRequest request, FaqDTO faqDTO);

    /**
     * FAQ 정보를 업데이트 합니다.
     * <p>
     * 이 메서드는 FAQ 정보({@code FaqDTO})를 받아 해당 FAQ의 정보를 업데이트합니다.
     * 성공적으로 업데이트되면, 성공 메시지를 담은 {@code ResponseEntity<String>}을 반환합니다.
     *
     * @param faqDTO 업데이트할 FAQ의 정보를 담고 있는 {@code FaqDTO} 객체.
     * @return 처리 결과를 담고 있는 {@code ResponseEntity<String>} 객체.
     */

    ResponseEntity<String> updateFaq(FaqDTO faqDTO);

    /**
     * 특정 FAQ를 삭제합니다.
     * <p>
     * 이 메서드는 FAQ 정보({@code FaqDTO})를 받아 해당 FAQ를 삭제합니다.
     * 성공적으로 삭제되면, 성공 메시지를 담은 {@code ResponseEntity<String>}을 반환합니다.
     *
     * @param faqDTO 삭제할 FAQ의 정보를 담고 있는 {@code FaqDTO} 객체.
     * @return 처리 결과를 담고 있는 {@code ResponseEntity<String>} 객체.
     */

    ResponseEntity<String> deleteFaq(FaqDTO faqDTO);

    /**
     * 서비스 이용 약관을 조회합니다.
     * <p>
     * 이 메서드는 현재 서비스의 이용 약관을 조회하고, 그 결과를 {@code JsonResult} 형태로 반환합니다.
     * 반환되는 {@code JsonResult} 객체에는 서비스 이용 약관에 관한 정보가 포함되어 있습니다.
     *
     * @return 현재 서비스 이용 약관을 담고 있는 {@code JsonResult} 객체.
     */

    JsonResult getTerms();

    /**
     * 서비스 이용 약관을 업데이트 합니다.
     * <p>
     * 이 메서드는 서비스 이용 약관 정보({@code TermsDTO})를 받아 해당 이용 약관을 업데이트합니다.
     * 성공적으로 업데이트되면, 성공 메시지를 담은 {@code ResponseEntity<String>}을 반환합니다.
     *
     * @param termsDTO 업데이트할 서비스 이용 약관의 정보를 담고 있는 {@code TermsDTO} 객체.
     * @return 처리 결과를 담고 있는 {@code ResponseEntity<String>} 객체.
     */

    ResponseEntity<String> updateTerms(TermsDTO termsDTO);


}