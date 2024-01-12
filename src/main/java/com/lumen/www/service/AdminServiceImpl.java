package com.lumen.www.service;

import com.lumen.www.dto.MonthlySubscriberDTO;
import com.lumen.www.json.JsonResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.lumen.www.dao.AdminRepository;
import com.lumen.www.dto.AdminUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    //private final JwtTokenProvider jwtTokenProvider;

    // 1차 로그인
    @Override
    @Transactional
    public JsonResult adminLogin(AdminUser adminUser) {
        System.out.println("adminLogin Service()");

        // 아이디 e-mail 형식확인
        if (isValidEmail(adminUser.getAdminId())) {
            AdminUser adminUserDB = adminRepository.adminLogin(adminUser);

            // adminUserDB가 null이 아닐 경우에만 결과 반환
            return createJsonResult(Optional.ofNullable(adminUserDB)
                    .map(AdminUser::getRole)
                    .orElse(null));
        } else {
            // 이메일 형식이 아닐 경우 적절한 에러 메시지 또는 코드 반환
            return createJsonResult("Invalid email format");
        }
    }

    // 2차 로그인
    @Override
    @Transactional
    public AdminUser adminLoginCk(AdminUser adminUser) { // 세션이나 토큰 사용하면 수정될수있음 직접빼서 확인해보면 되니깐 권한
        System.out.println("adminLoginCk Service()");
        return adminRepository.adminLoginCk(adminUser);
    }

    // 가입자 현황
    @Override
    @Transactional
    public JsonResult subscriberCount() {
        System.out.println("subscriberCount Service()");
        return createJsonResult(adminRepository.subscriberCount());
    }

    // 메인페이지 월별가입자 그래프
    @Override
    @Transactional
    public JsonResult getMonthlySalesChart() {
        System.out.println("monthlySalesChart Service()");

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
        System.out.println("currentSituation Service()");
        return createJsonResult(adminRepository.getUserActivity());
    }

    // 메인페이지 문의현황
    @Override
    @Transactional
    public JsonResult getMainInquiryList() {
        System.out.println("mainInquiryList Service()");
        return createJsonResult(adminRepository.getInquiryList());
    }

    // JsonResult 생성 & 반환
    private JsonResult createJsonResult(Object data) {
        System.out.println(data);
        JsonResult jsonResult = new JsonResult();
        if (data != null) jsonResult.success(data);
        else jsonResult.fail("실패");
        return jsonResult;
    }

    // 특수 문자를 제거하는 유틸리티 메서드
    public boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
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
}

