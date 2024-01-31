package com.lumen.www.service;

import com.lumen.www.dao.AdminRepository;
import com.lumen.www.dto.*;
import com.lumen.www.jwt.JwtTokenProvider;
import com.lumen.www.util.EmailService;
import com.lumen.www.util.JwtTokenUtil;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private static final String LOG_FAILURE_MESSAGE = "실패";
    private final AdminRepository adminRepository;
    private final EmailService emailService;
    private final JwtTokenProvider jwtTokenProvider;


    // 2차 로그인
    @Override
    @Transactional
    public ResponseEntity<?> adminLoginCk(AdminUser adminUser) {
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

    // 관리자 정보 가져오기
    @Override
    @Transactional
    public JsonResult getAdminUser(HttpServletRequest request) {
        // 1. 토큰 추출
        String token = JwtTokenUtil.resolveToken(request);

        // 2. 사용자 정보 조회
        AdminDTO adminDTO = adminRepository.getAdminUser(jwtTokenProvider.getAdminUserInfoFromToken(token));

        System.out.println("2" + adminDTO);
        if (adminDTO == null) {
            // 사용자 정보가 없는 경우의 처리
            return createJsonResult(null);
        }

        // 4. 결과 반환
        return createJsonResult(adminDTO);
    }

    // 가입자 현황
    @Override
    @Transactional
    public JsonResult subscriberCount() {
        return createJsonResult(adminRepository.subscriberCount());
    }

    // 메인페이지 월별가입자 그래프
    @Override
    @Transactional
    public JsonResult getMonthlySalesChart() {
        List<Map<String, Object>> resultList = adminRepository.getMonthlySubscriber();

        // 'resultList'를 'monthList'로 변환
        List<String> monthList = resultList.stream()
                // 'resultList'의 각 요소에서 'month' 키의 값을 추출하고 String으로 형변환
                .map(resultMap -> (String) resultMap.get("month"))
                // 변환된 값들을 리스트로 수집
                .collect(Collectors.toList());

        // 'resultList'를 'subscribersCountList'로 변환
        List<Integer> subscribersCountList = resultList.stream()
                // 'resultList'의 각 요소에서 'subscribers_count' 키의 값을 추출하고 Integer로 형변환
                .map(resultMap -> (Integer) resultMap.get("subscribers_count"))
                // 변환된 값들을 리스트로 수집
                .collect(Collectors.toList());

        MonthlySubscriberDTO monthlySubscriberDTO = new MonthlySubscriberDTO(monthList, subscribersCountList);

        return createJsonResult(monthlySubscriberDTO);
    }

    // 메인페이지 현황지표
    @Override
    @Transactional
    public JsonResult getCurrentSituation() {
        return createJsonResult(adminRepository.getUserActivity());
    }

    // 메인페이지 문의현황
    @Override
    @Transactional
    public JsonResult getMainInquiryList() {
        return createJsonResult(adminRepository.getInquiryList());
    }

    // 가입자 리스트
    @Override
    @Transactional
    public JsonResult getJoinList(SearchDTO searchDTO) {
        return createJsonResult(adminRepository.getJoinList(searchDTO));
    }

    // 가입자 세부정보
    @Override
    public JsonResult getUserDetails(UserDTO userDTO) {
        return createJsonResult(adminRepository.getUserDetails(userDTO));
    }

    // 프로모션 메일 발송
    @Override
    public JsonResult addPromotions(PromotionsDTO promotionsDTO) {

        EmailMessage emailMessage = EmailMessage.builder().subject(promotionsDTO.getPromotionsTitle()).message(promotionsDTO.getPromotionsContent()).build();
        // 이메일 전송 후 결과를 반환받음
        String result = emailService.sendMail(emailMessage, "test");

        if (result.equals("ok")) {
            return createJsonResult(result);
        } else return null;
    }

    // 가입자 강제탈퇴
    @Override
    @Transactional
    public ResponseEntity<?> adminJoinUserDelete(UserDTO userDTO) {

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

    // 비밀번호 변경 메일발송
    @Override
    public JsonResult adminJoinPWReset(UserDTO userDTO) {

        EmailMessage emailMessage = EmailMessage.builder().subject("비밀번호 초기화 메일알림").message("비밀번호 초기화 해주세요").build();
        // 이메일 전송 후 결과를 반환받음
        String result = emailService.sendMail2(emailMessage, "menstua@viking-lab.com");

        if (result.equals("ok")) {
            return createJsonResult(result);
        } else return null;
    }

    // 미결제 회원 리스트
    @Override
    @Transactional
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
        System.out.println(priceSearchDTO);
        return createJsonResult(adminRepository.getPriceList(priceSearchDTO));
    }

    // 회원 상태 변경
    @Override
    @Transactional
    public ResponseEntity<?> updateUserStatus(UserDTO userDTO) {

        int result = adminRepository.updateUserStatus(userDTO);
        if (result > 0) {
            // 성공적으로 하나 이상의 행이 업데이트되었을 때
            return ResponseEntity.ok().body("User status successfully updated.");
        } else {
            // 업데이트할 행이 없거나 실패했을 때
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to update user status.");
        }
    }

    // 청약철회 현황
    @Override
    @Transactional
    public JsonResult getSubscriptionEndList(SearchDTO searchDTO) {
        return createJsonResult(adminRepository.getSubscriptionEndList(searchDTO));
    }

    // 인보이스 리스트
    @Override
    @Transactional
    public JsonResult getInvoiceList(SearchDTO searchDTO) {
        return createJsonResult(adminRepository.getInvoiceList(searchDTO));
    }

    // 인보이스 세부정보
    @Override
    @Transactional
    public JsonResult getInvoiceDetails(InvoiceDTO invoiceDTO) {
        return createJsonResult(adminRepository.getInvoiceDetails(invoiceDTO));
    }

    // JsonResult 생성 & 반환
    private JsonResult createJsonResult(Object data) {
        System.out.println("리턴 " + data);
        JsonResult jsonResult = new JsonResult();
        if (data != null) jsonResult.success(data);
        else jsonResult.fail(LOG_FAILURE_MESSAGE);
        return jsonResult;
    }

}
