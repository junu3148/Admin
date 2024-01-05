package vikinglab.lumen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vikinglab.lumen.dao.AdminRepository;
import vikinglab.lumen.json.JsonResult;
import vikinglab.lumen.vo.AdminUser;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    //private final JwtTokenProvider jwtTokenProvider;

    // 1차 로그인
    @Override
    public JsonResult adminLogin(AdminUser adminUser) {
        System.out.println("adminLogin Service()");

        // 아이디 유효성 검사 특수문자 제거
        adminUser.setId(removeSpecialCharacters(adminUser.getId()));

        AdminUser adminUserDB = adminRepository.adminLogin(adminUser);

        return createJsonResult(Optional.ofNullable(adminUserDB)
                .map(AdminUser::getIsAdmin)
                .orElse(null));
    }

    // 2차 로그인
    @Override
    public AdminUser adminLoginCk(AdminUser adminUser) { // 세션이나 토큰 사용하면 수정될수있음 직접빼서 확인해보면 되니깐 권한
        System.out.println("adminLoginCk Service()");

        // 슈퍼 어드민일 경우
        boolean isAccountInfoProvided = adminUser.getAccountName() != null && adminUser.getAccountNumber() != null;
        return isAccountInfoProvided
                ? adminRepository.adminLoginCk(adminUser)
                : adminRepository.adminLoginCk(adminUser.getUserName());
    }

    // 가입자 현황
    @Override
    public JsonResult subscriberCount() {
        System.out.println("subscriberCount Service()");
        return createJsonResult(adminRepository.subscriberCount());
    }

    // 메인페이지 월별가입자 그래프
    @Override
    public JsonResult getMonthlySalesChart() {
        System.out.println("monthlySalesChart Service()");
        return createJsonResult(adminRepository.getMonthlySalesChart());
    }

    // 메인페이지 현황지표
    @Override
    public JsonResult getCurrentSituation() {
        System.out.println("currentSituation Service()");
        return createJsonResult(adminRepository.getCurrentSituation());
    }

    // 메인페이지 문의현황
    @Override
    public JsonResult getMainInquiryList() {
        System.out.println("mainInquiryList Service()");
        return createJsonResult(adminRepository.getMainInquiryList());
    }

    // JsonResult 생성 & 반환
    private JsonResult createJsonResult(Object data) {

        JsonResult jsonResult = new JsonResult();
        if (data != null) jsonResult.success(data);
        else jsonResult.fail("실패");
        return jsonResult;
    }

    // 특수 문자를 제거하는 유틸리티 메서드
    private String removeSpecialCharacters(String input) {
        // 특수 문자를 제거하는 정규 표현식 패턴
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        Matcher matcher = pattern.matcher(input);

        // 특수 문자를 제거한 결과 문자열 반환
        return matcher.replaceAll("");
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

