package com.lumen.www.service;

import com.lumen.www.dao.AdminRepository;
import com.lumen.www.dto.*;
import com.lumen.www.entity.EmailMessage;
import com.lumen.www.exception.ServiceException;
import com.lumen.www.jwt.JwtTokenProvider;
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

    private static final Pattern EMAIL_PATTERN = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    private static final String INVALID_EMAIL_FORMAT = "Invalid email format";
    private static final String LOG_FAILURE_MESSAGE = "실패";

    private final AdminRepository adminRepository;

    private final EmailService emailService;

    private final JwtTokenProvider jwtTokenProvider;



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


    @Override
    public JsonResult getJoinList(JoinSearchDTO joinSearchDTO){
            List<JoinListDTO> joinListDTO = adminRepository.getJoinList(joinSearchDTO);
            return createJsonResult(adminRepository.getJoinList(joinSearchDTO));
    }


    // 프로모션 메일 발송
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
        JsonResult jsonResult = new JsonResult();
        if (data != null) jsonResult.success(data);
        else jsonResult.fail(LOG_FAILURE_MESSAGE);
        return jsonResult;
    }

    // 이메일 형식 체크
    private boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }
}
