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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private static final String LOG_FAILURE_MESSAGE = "실패";

    private final AdminRepository adminRepository;

    private final EmailService emailService;


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
    @Transactional
    public JsonResult getJoinList(JoinSearchDTO joinSearchDTO) {
        List<JoinListDTO> joinListDTO = adminRepository.getJoinList(joinSearchDTO);
        return createJsonResult(adminRepository.getJoinList(joinSearchDTO));
    }


    // 프로모션 메일 발송
    @Override
    public JsonResult addPromotions(PromotionsDTO promotionsDTO) {
        System.out.println(promotionsDTO);

        EmailMessage emailMessage = EmailMessage.builder().subject(promotionsDTO.getPromotionsTitle()).message(promotionsDTO.getPromotionsContent()).build();
        // 이메일 전송 후 결과를 반환받음
        String result = emailService.sendMail(emailMessage, "test");
        System.out.println(result);
        if (result.equals("ok")) {
            return createJsonResult(result);
        } else return null;
    }


    // JsonResult 생성 & 반환
    private JsonResult createJsonResult(Object data) {
        JsonResult jsonResult = new JsonResult();
        if (data != null) jsonResult.success(data);
        else jsonResult.fail(LOG_FAILURE_MESSAGE);
        return jsonResult;
    }


}
