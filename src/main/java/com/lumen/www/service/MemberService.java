package com.lumen.www.service;

import com.lumen.www.dto.auth.JwtToken;
import com.lumen.www.dto.user.AdminUser;
import org.springframework.http.ResponseEntity;


public interface MemberService {

    /**
     * 사용자 로그인을 처리하고, JWT 토큰을 생성하여 반환하는 메서드입니다.
     *
     * @param adminUser 로그인에 사용할 관리자 사용자 정보를 포함하는 객체입니다.
     * @return 성공적으로 로그인하고 토큰을 생성한 경우, 해당 토큰을 포함하는 ResponseEntity를 반환합니다.
     * 유효하지 않은 이메일이 입력된 경우, BAD_REQUEST 상태와 메시지를 포함하는 ResponseEntity를 반환합니다.
     * 인증이 실패한 경우, UNAUTHORIZED 상태와 메시지를 포함하는 ResponseEntity를 반환합니다.
     */

    ResponseEntity<JwtToken> signInAndGenerateJwtToken(AdminUser adminUser);
    /**
     * 리프레시 토큰의 유효성을 검사한 후, 새로운 액세스 토큰을 발행하여 반환하는 메서드입니다.
     *
     * @param refreshToken 리프레시 토큰입니다.
     * @return 새로운 액세스 토큰을 포함하는 ResponseEntity를 반환합니다.
     * 유효하지 않은 리프레시 토큰인 경우, UNAUTHORIZED 상태와 메시지를 포함하는 ResponseEntity를 반환합니다.
     * 예외가 발생한 경우, INTERNAL_SERVER_ERROR 상태와 메시지를 포함하는 ResponseEntity를 반환합니다.
     */
    ResponseEntity<String> refreshTokenCK(String refreshToken);


}
