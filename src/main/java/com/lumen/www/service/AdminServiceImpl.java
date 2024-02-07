package com.lumen.www.service;

import com.lumen.www.dao.AdminRepository;
import com.lumen.www.dao.TokenRepository;
import com.lumen.www.dto.common.JsonResult;
import com.lumen.www.dto.common.SearchDTO;
import com.lumen.www.dto.faq.FaqDTO;
import com.lumen.www.dto.inquiry.InquiryDTO;
import com.lumen.www.dto.inquiry.InquiryListDTO;
import com.lumen.www.dto.invoice.InvoiceDTO;
import com.lumen.www.dto.invoice.InvoiceListDTO;
import com.lumen.www.dto.main.MonthlySubscriberDTO;
import com.lumen.www.dto.notice.NoticeDTO;
import com.lumen.www.dto.notice.NoticeListDTO;
import com.lumen.www.dto.payment.PayListDTO;
import com.lumen.www.dto.pricing.PriceListDTO;
import com.lumen.www.dto.pricing.PriceSearchDTO;
import com.lumen.www.dto.terms.TermsDTO;
import com.lumen.www.dto.user.AdminDTO;
import com.lumen.www.dto.user.AdminUser;
import com.lumen.www.dto.user.JoinListDTO;
import com.lumen.www.dto.user.UserDTO;
import com.lumen.www.jwt.JwtTokenProvider;
import com.lumen.www.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private static final String LOG_FAILURE_MESSAGE = "실패";
    private static final String NO_INQUIRY_RESULTS = "조회 결과가 없습니다.";
    private final AdminRepository adminRepository;
    private final EmailService emailService;
    private final JwtTokenProvider jwtTokenProvider;
    private final InvoiceService invoiceService;
    private final TokenRepository tokenRepository;

    // 2차 로그인
    @Override
    @Transactional
    public ResponseEntity<String> adminLoginCk(AdminUser adminUser) {
        // adminRepository를 사용하여 AdminUser 객체를 조회
        Optional<AdminUser> result = adminRepository.adminLoginCk(adminUser);
        // 결과가 존재하는 경우 (사용자가 있음)
        if (result.isPresent()) {
            // HttpStatus.OK와 함께 사용자 객체 반환
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            // 사용자가 없는 경우, HttpStatus.NOT_FOUND와 에러 메시지 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    // 관리자 세부 정보
    @Override
    @Transactional(readOnly = true) // 정보를 가져올수만 있음.
    public JsonResult getAdminUser(HttpServletRequest request) {
        // 1. 토큰 추출
        String token = JwtTokenUtil.resolveToken(request);

        // 2. 사용자 정보 조회
        AdminDTO adminDTO = adminRepository.getAdminUser(jwtTokenProvider.getAdminUserInfoFromToken(token));

        if (adminDTO == null) {
            // 사용자 정보가 없는 경우의 처리
            return createJsonResult(null);
        }

        // 4. 결과 반환
        return createJsonResult(adminDTO);
    }

    // 관리자 정보 수정
    @Override
    @Transactional
    //@PreAuthorize("#adminUser.username == authentication.principal.username") //현재 인증된 사용자가 수정하려는 AdminUser의 adminId와 일치하는 경우에만 메서드 실행이 진행됩니다.
    //@PreAuthorize("#adminUser.getUsername() == authentication.principal.username")
    public ResponseEntity<String> updateAdminUser(AdminUser adminUser) {


        if (adminUser.getPassword() != null) {
            // 비밀번호 BCrypt 변환
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(adminUser.getPassword());
            adminUser.setAdminPassword(encodedPassword);
        }
        int result = adminRepository.updateAdminUser(adminUser);

        return createResponse(result, "Modifying administrator information was performed normally.", "Modifying administrator information failed.");
    }

    // 관리자 로그 아웃(리플레시 삭제)
    @Override
    @Transactional
    public void logout(HttpServletRequest request) {

        String token = JwtTokenUtil.resolveToken(request);

        String adminId = jwtTokenProvider.getAdminUserInfoFromToken(token);

        tokenRepository.deleteRefreshToken(adminId);

    }





    // ------------------------------------------------------------------------------------- Main -----------------------------------------------------------------------------------------
    // 가입자 현황
    @Override
    @Transactional(readOnly = true)
    public JsonResult subscriberCount() {
        return createJsonResult(adminRepository.subscriberCount());
    }

    // 메인페이지 월별가입자 그래프
    @Override
    @Transactional(readOnly = true)
    public JsonResult getMonthlySalesChart() {

        List<Map<String, Object>> resultList = adminRepository.getMonthlySubscriber();

        List<String> monthList = new ArrayList<>();
        for (Map<String, Object> stringObjectMap : resultList) {
            String month = (String) stringObjectMap.get("month");
            monthList.add(month);
        }

        List<Integer> subscribersCountList = new ArrayList<>();
        for (Map<String, Object> resultMap : resultList) {
            Integer subscribersCount = ((Number) resultMap.get("subscribers_count")).intValue();
            subscribersCountList.add(subscribersCount);
        }

        MonthlySubscriberDTO monthlySubscriberDTO = new MonthlySubscriberDTO(monthList, subscribersCountList);

        return createJsonResult(monthlySubscriberDTO);
    }

    // 메인페이지 현황지표
    @Override
    @Transactional(readOnly = true)
    public JsonResult getCurrentSituation() {
        return createJsonResult(adminRepository.getUserActivity());
    }

    // 메인페이지 문의현황
    @Override
    @Transactional(readOnly = true)
    public JsonResult getMainInquiryList() {
        return createJsonResult(adminRepository.getInquiryList());
    }





    // ------------------------------------------------------------------------------------- Join -----------------------------------------------------------------------------------------
    // 가입자 리스트
    @Override
    @Transactional(readOnly = true)
    public JsonResult getJoinList(SearchDTO searchDTO) {


        List<JoinListDTO> joinListDTOS = adminRepository.getJoinList(searchDTO);

        createListWithDefaultValueIfEmpty(joinListDTOS, () -> {
            JoinListDTO joinListDTO = new JoinListDTO();
            joinListDTO.set이메일(NO_INQUIRY_RESULTS); // 상수값
            return joinListDTO;
        });

        return createJsonResult(joinListDTOS);
    }

    // 가입자 세부 정보
    @Override
    @Transactional(readOnly = true)
    public JsonResult getUserDetails(UserDTO userDTO) {
        return createJsonResult(adminRepository.getUserDetails(userDTO));
    }

    // 가입자 강제 탈퇴
    @Override
    @Transactional
    public ResponseEntity<String> adminJoinUserDelete(UserDTO userDTO) {

        int result = adminRepository.adminJoinUserDelete(userDTO);
        if (result == 1) {
            // 삭제 성공
            return ResponseEntity.ok().body("User successfully deleted.");
        } else if (result == 0) {
            // 삭제할 대상 없음
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user found to delete.");
        } else {
            // 기타 오류
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during deletion.");
        }
    }





    // ------------------------------------------------------------------------------------- Price -----------------------------------------------------------------------------------------
    // 미결제 회원 현황
    @Override
    @Transactional(readOnly = true)
    public JsonResult getPriceList(PriceSearchDTO priceSearchDTO) {

        final String outInfo = priceSearchDTO.getOutInfo();
        if (outInfo != null) {
            final boolean isTenTimesDefaulted = outInfo.startsWith("10");
            switch (outInfo.charAt(0)) {
                case '1':
                    priceSearchDTO.setMinNum(isTenTimesDefaulted ? 10 : 1);
                    priceSearchDTO.setMaxNum(isTenTimesDefaulted ? 10 : 4);
                    break;
                case '5':
                    priceSearchDTO.setMinNum(5);
                    priceSearchDTO.setMaxNum(9);
                    break;
                default:
                    priceSearchDTO.setMinNum(1);
                    priceSearchDTO.setMaxNum(10);
            }
        }

        List<PriceListDTO> priceListDTOS = adminRepository.getPriceList(priceSearchDTO);

        createListWithDefaultValueIfEmpty(priceListDTOS, () -> {
            PriceListDTO priceListDTO = new PriceListDTO();
            priceListDTO.set이메일(NO_INQUIRY_RESULTS); // 상수값
            return priceListDTO;
        });

        return createJsonResult(priceListDTOS);
    }

    // 회원 상태 변경
    @Override
    @Transactional
    public ResponseEntity<String> updateUserStatus(UserDTO userDTO) {
        int result = adminRepository.updateUserStatus(userDTO);
        return createResponse(result, "User status successfully updated.", "Failed to update user status.");
    }





    // ------------------------------------------------------------------------------------- Pay -----------------------------------------------------------------------------------------
    // 청약철회 현황
    @Override
    @Transactional(readOnly = true)
    public JsonResult getSubscriptionEndList(SearchDTO searchDTO) {


        List<PayListDTO> payListDTOS = adminRepository.getSubscriptionEndList(searchDTO);

        createListWithDefaultValueIfEmpty(payListDTOS, () -> {
            PayListDTO payListDTO = new PayListDTO();
            payListDTO.set이메일(NO_INQUIRY_RESULTS); // 상수값
            return payListDTO;
        });

        return createJsonResult(payListDTOS);
    }





    // ------------------------------------------------------------------------------------- Invoice -----------------------------------------------------------------------------------------
    // 인보이스 리스트
    @Override
    @Transactional(readOnly = true)
    public JsonResult getInvoiceList(SearchDTO searchDTO) {

        List<InvoiceListDTO> invoiceDTOS = adminRepository.getInvoiceList(searchDTO);

        createListWithDefaultValueIfEmpty(invoiceDTOS, () -> {
            InvoiceListDTO invoiceListDTO = new InvoiceListDTO();
            invoiceListDTO.set이메일(NO_INQUIRY_RESULTS); // 상수값
            return invoiceListDTO;
        });

        return createJsonResult(invoiceDTOS);
    }

    // 인보이스 세부 정보
    @Override
    @Transactional(readOnly = true)
    public JsonResult getInvoiceDetails(InvoiceDTO invoiceDTO) {
        return createJsonResult(adminRepository.getInvoiceDetails(invoiceDTO));
    }

    // 인보이스 메일 발송
    @Override
    public ResponseEntity<String> invoiceEmailShipment(InvoiceDTO invoiceDTO) {

        InvoiceDTO invoice = adminRepository.getInvoiceDetails(invoiceDTO);

        try {
            invoiceService.sendInvoiceAsEmail(invoice.getUserId(), invoice);
        } catch (Exception e) {

            return new ResponseEntity<>("Failed to send invoice email.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return null;
    }





    // ------------------------------------------------------------------------------------- 1:1 -----------------------------------------------------------------------------------------
    // 1:1 문의 현황
    @Override
    @Transactional(readOnly = true)
    public JsonResult getInquiryList(SearchDTO searchDTO) {

        List<InquiryListDTO> inquiryListDTOS = adminRepository.getInquiryList(searchDTO);

        createListWithDefaultValueIfEmpty(inquiryListDTOS, () -> {
            InquiryListDTO inquiryListDTO = new InquiryListDTO();
            inquiryListDTO.set이메일(NO_INQUIRY_RESULTS); // 상수값
            return inquiryListDTO;
        });

        return createJsonResult(inquiryListDTOS);

    }


    // 1:1 문의 세부 정보
    @Override
    @Transactional(readOnly = true)
    public JsonResult getInquiryDetails(InquiryDTO inquiryDTO) {
        return createJsonResult(adminRepository.getInquiryDetails(inquiryDTO));
    }

    // 1:1 문의 답변 등록
    @Override
    @Transactional
    public ResponseEntity<String> insertInquiryAnswer(InquiryDTO inquiryDTO) {
        int result = adminRepository.insertInquiryAnswer(inquiryDTO);
        return createResponse(result, "Your inquiry has been successfully registered.", "The answer to the inquiry failed.");
    }





    // ------------------------------------------------------------------------------------- Notice -----------------------------------------------------------------------------------------
    // 공지사항 현황
    @Override
    @Transactional(readOnly = true)
    public JsonResult getNoticeList(SearchDTO searchDTO) {

        List<NoticeListDTO> noticeListDTOS = adminRepository.getNoticeList(searchDTO);

        createListWithDefaultValueIfEmpty(noticeListDTOS, () -> {
            NoticeListDTO noticeListDTO = new NoticeListDTO();
            noticeListDTO.set제목(NO_INQUIRY_RESULTS); // 상수값
            return noticeListDTO;
        });

        return createJsonResult(noticeListDTOS);
    }

    // 공지사항 세부 정보
    @Override
    @Transactional(readOnly = true)
    public JsonResult getNoticeDetails(NoticeDTO noticeDTO) {
        return createJsonResult(adminRepository.getNoticeDetails(noticeDTO));
    }

    // 공지사항 등록
    @Override
    @Transactional
    public ResponseEntity<String> insertNotice(HttpServletRequest request, NoticeDTO noticeDTO) {

        // 토큰 추출
        String token = JwtTokenUtil.resolveToken(request);

        // 토큰에서 아이디 추출
        String userId = jwtTokenProvider.getAdminUserInfoFromToken(token);
        noticeDTO.setAdminId(userId);

        int result = adminRepository.insertNotice(noticeDTO);
        return createResponse(result, "Notice registration has been successfully executed.", "Announcement registration failed.");
    }

    // 공지사항 수정
    @Override
    @Transactional
    public ResponseEntity<String> updateNotice(NoticeDTO noticeDTO) {
        int result = adminRepository.updateNotice(noticeDTO);
        return createResponse(result, "The announcement has been revised to a successful one.", "Failed to modify the announcement.");
    }

    // 공지사항 삭제
    @Override
    @Transactional
    public ResponseEntity<String> deleteNotice(NoticeDTO noticeDTO) {
        int result = adminRepository.deleteNotice(noticeDTO);
        return createResponse(result, "Delete Announcement has been successfully executed.", "Failed to delete announcement.");
    }





    // ------------------------------------------------------------------------------------- FAQ -----------------------------------------------------------------------------------------
    // FAQ 현황
    @Override
    @Transactional(readOnly = true)
    public JsonResult getFaqList(SearchDTO searchDTO) {

        List<FaqDTO> faqDTOS = adminRepository.getFaqList(searchDTO);

        createListWithDefaultValueIfEmpty(faqDTOS, FaqDTO::new);

        return createJsonResult(adminRepository.getFaqList(searchDTO));
    }

    // FAQ 세부 정보
    @Override
    @Transactional(readOnly = true)
    public JsonResult getFaq(FaqDTO faqDTO) {
        return createJsonResult(adminRepository.getFaq(faqDTO));
    }

    // FAQ 등록
    @Override
    @Transactional
    public ResponseEntity<String> insertFaq(HttpServletRequest request, FaqDTO faqDTO) {

        // 토큰 추출
        String token = JwtTokenUtil.resolveToken(request);

        // 토큰에서 아이디 추출
        String userId = jwtTokenProvider.getAdminUserInfoFromToken(token);
        faqDTO.setAdminId(userId);

        int result = adminRepository.insertFaq(faqDTO);

        return createResponse(result, "FAQ registration has been executed successfully.", "FAQ registration failed.");
    }

    // FAQ 수정
    @Override
    @Transactional
    public ResponseEntity<String> updateFaq(FaqDTO faqDTO) {

        int result = adminRepository.updateFaq(faqDTO);

        return createResponse(result, "The FAQ modification was executed successfully.", "FAQ modification failed.");
    }

    // FAQ 삭제
    @Override
    @Transactional
    public ResponseEntity<String> deleteFaq(FaqDTO faqDTO) {

        int result = adminRepository.deleteFaq(faqDTO);

        return createResponse(result, "FAQ deletion has been successfully executed.", "Failed to delete FAQ.");
    }





    // ------------------------------------------------------------------------------------- Terms -----------------------------------------------------------------------------------------
    // Terms 정보
    @Override
    @Transactional(readOnly = true)
    public JsonResult getTerms() {
        return createJsonResult(adminRepository.getTerms());
    }

    // Terms 수정
    @Override
    @Transactional
    public ResponseEntity<String> updateTerms(TermsDTO termsDTO) {

        int result = adminRepository.updateTerms(termsDTO);

        return createResponse(result, "FAQ registration has been executed successfully.", "FAQ registration failed.");
    }





    // ------------------------------------------------------------------------------------- 공통로직 -----------------------------------------------------------------------------------------
    // 공통 응답 생성 메서드
    private ResponseEntity<String> createResponse(int result, String successMessage, String failureMessage) {
        if (result > 0) {
            // 성공적으로 하나 이상의 행이 업데이트되었을 때
            return ResponseEntity.ok().body(successMessage);
        } else {
            // 업데이트할 행이 없거나 실패했을 때
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(failureMessage);
        }
    }

    // JsonResult 생성 & 반환
    private JsonResult createJsonResult(Object data) {
        JsonResult jsonResult = new JsonResult();
        if (data != null) jsonResult.success(data);
        else jsonResult.fail(LOG_FAILURE_MESSAGE);
        return jsonResult;
    }

    // 함수형 인터페이스를 매개변수로 받는 범용 메소드
    private <T> void createListWithDefaultValueIfEmpty(List<T> list, Supplier<T> defaultSupplier) {
        if (list.isEmpty()) {
            T defaultValue = defaultSupplier.get(); // 기본 객체 생성
            list.add(defaultValue);
        }
    }
}
