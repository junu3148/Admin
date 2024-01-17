package com.lumen.www.service;

import com.lumen.www.dao.AdminRepository;
import com.lumen.www.dto.AdminUser;
import com.lumen.www.dto.JsonResult;
import com.lumen.www.dto.MonthlySubscriberDTO;
import com.lumen.www.dto.PromotionsDTO;
import com.lumen.www.entity.EmailMessage;
import com.lumen.www.exception.ServiceException;
import com.lumen.www.util.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);
    private static final Pattern EMAIL_PATTERN = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    private static final String INVALID_EMAIL_FORMAT = "Invalid email format";
    private static final String LOG_FAILURE_MESSAGE = "실패";

    private final AdminRepository adminRepository;

    private final EmailService emailService;


    // 1차 로그인
    @Override
    @Transactional
    public JsonResult adminLogin(AdminUser adminUser) {
        logger.debug("adminLogin Service() called with adminUser: {}", adminUser);

        if (isValidEmail(adminUser.getAdminId())) {
            try {
                AdminUser adminUserDB = adminRepository.adminLogin(adminUser);
                return createJsonResult(Optional.ofNullable(adminUserDB).map(AdminUser::getRole).orElse(null));
            } catch (Exception e) {
                logger.error("Error in adminLogin", e);
                return createJsonResult(LOG_FAILURE_MESSAGE);
            }
        } else {
            return createJsonResult(INVALID_EMAIL_FORMAT);
        }
    }

    // 2차 로그인
    @Override
    @Transactional
    public AdminUser adminLoginCk(AdminUser adminUser) {
        logger.debug("adminLoginCk Service() called with adminUser: {}", adminUser);
        try {
            return adminRepository.adminLoginCk(adminUser);
        } catch (Exception e) {
            logger.error("Error in adminLoginCk", e);
            throw new ServiceException("Error in adminLoginCk", e);
        }
    }

    // 가입자 현황
    @Override
    @Transactional
    public JsonResult subscriberCount() {
        logger.debug("subscriberCount Service()");
        try {
            return createJsonResult(adminRepository.subscriberCount());
        } catch (Exception e) {
            logger.error("Error in subscriberCount", e);
            return createJsonResult(LOG_FAILURE_MESSAGE);
        }
    }

    // 메인페이지 월별가입자 그래프
    @Override
    @Transactional
    public JsonResult getMonthlySalesChart() {
        logger.debug("monthlySalesChart Service()");
        try {
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
        } catch (Exception e) {
            logger.error("Error in getMonthlySalesChart", e);
            return createJsonResult(LOG_FAILURE_MESSAGE);
        }
    }

    // 메인페이지 현황지표
    @Override
    @Transactional
    public JsonResult getCurrentSituation() {
        logger.debug("currentSituation Service()");
        try {
            return createJsonResult(adminRepository.getUserActivity());
        } catch (Exception e) {
            logger.error("Error in getCurrentSituation", e);
            return createJsonResult(LOG_FAILURE_MESSAGE);
        }
    }

    // 메인페이지 문의현황
    @Override
    @Transactional
    public JsonResult getMainInquiryList() {
        logger.debug("mainInquiryList Service()");
        try {
            return createJsonResult(adminRepository.getInquiryList());
        } catch (Exception e) {
            logger.error("Error in getMainInquiryList", e);
            return createJsonResult(LOG_FAILURE_MESSAGE);
        }
    }

    // 공통된 로직은 프라이빗 메소드로 추출
    private boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    // 프로모션 배포
    @Override
    public JsonResult addPromotions(PromotionsDTO promotionsDTO) {
        System.out.println(promotionsDTO);

        EmailMessage emailMessage = EmailMessage.builder()
                .subject(promotionsDTO.getPromotionsTitle())
                .message(promotionsDTO.getPromotionsContent())
                .build();
        // 이메일 전송 후 결과를 반환받음
        String result = emailService.sendMail(emailMessage, "test");
        System.out.println(result);
        if (result.equals("ok")) {
            return createJsonResult(result);
        } else return null;
    }

    // JsonResult 생성 & 반환
    private JsonResult createJsonResult(Object data) {
        System.out.println(data);
        logger.debug("Creating JsonResult for data: {}", data);
        JsonResult jsonResult = new JsonResult();
        if (data != null) jsonResult.success(data);
        else jsonResult.fail(LOG_FAILURE_MESSAGE);
        return jsonResult;
    }
}




    /*  // 토큰생성 반환 서비스
    @Override
    public String loginck(AdminUser adminUser) {

        AdminUser adminUserDB;
        if (adminUser.getAccountName() != null && adminUser.getAccountNumber() != null) {
            adminUserDB = adminRepository.loginck(adminUser);
        } else {
            adminUserDB = adminRepository.loginck(adminUser.getUserName());
        }
        if (adminUserDB != null && adminUserDB.getUserName().equals(adminUser.getUserName())) {
            // 사용자 인증이 성공하면 토큰 생성
            return jwtTokenProvider.generateToken(adminUserDB);
        }
        return null; // 인증 실패 시 null 반환
    }

    // 서비스에서 쿠키에 토큰담기
    public ResponseCookie loginck2(AdminUser adminUser) {
        AdminUser adminUserDB = adminRepository.loginck(adminUser);

        if (adminUserDB != null && adminUserDB.getUserName().equals(adminUser.getUserName())) {
            // 사용자 인증이 성공하면 토큰 생성
            String token = jwtTokenProvider.generateToken(adminUserDB);

            // 토큰을 쿠키에 담아 반환
            return ResponseCookie.from("accessToken", token)
                    .httpOnly(true)
                    .sameSite("None")
                    .secure(true)
                    .path("/")
                    .maxAge(1800) // 30분
                    .domain("192.168.0.16")
                    .build();
        }

        return null; // 인증 실패 시 null 반환
    }*/

