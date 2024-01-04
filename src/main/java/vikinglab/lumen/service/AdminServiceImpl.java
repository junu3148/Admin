package vikinglab.lumen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import vikinglab.lumen.dao.AdminRepository;
import vikinglab.lumen.jwt.JwtTokenProvider;
import vikinglab.lumen.vo.AdminUser;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminDAO;
    private final JwtTokenProvider jwtTokenProvider;


    @Override
    public AdminUser login(AdminUser adminUser) {
        System.out.println("AdminSeviceImpl login()");
        return adminDAO.login(adminUser);
    }

    // 토큰생성 반환 서비스
    @Override
    public String loginck(AdminUser adminUser) {
        AdminUser adminUserDB = adminDAO.loginck(adminUser);

        if (adminUserDB != null && adminUserDB.getUserName().equals(adminUser.getUserName())) {
            // 사용자 인증이 성공하면 토큰 생성
            return jwtTokenProvider.generateToken(adminUserDB);
        }
        return null; // 인증 실패 시 null 반환
    }

    // 서비스에서 쿠키에 토큰담기
    public ResponseCookie loginck2(AdminUser adminUser) {
        AdminUser adminUserDB = adminDAO.loginck(adminUser);

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
    }
}

