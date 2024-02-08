package com.lumen.www.service;

import com.lumen.www.dto.promotion.PromotionsDTO;
import com.lumen.www.dto.user.UserDTO;
import org.springframework.http.ResponseEntity;

public interface EmailService {


    /**
     * 주어진 이메일 메시지를 특정 수신자들에게 비동기적으로 발송합니다.
     * <p>
     * 이 메서드는 정의된 수신자 목록에 대해 각각 비동기 이메일 발송 메서드(sendMailAsync)를 호출합니다.
     * 이메일 전송이 성공하면 콘솔에 서비스 시간을 출력하고, 실패하면 로그에 오류를 기록하고 RuntimeException을 발생시킵니다.
     * 모든 이메일 전송 시도 후에는 "ok" 문자열을 반환하여 전송이 시작되었음을 나타냅니다.
     *
     * @param promotionsDTO 이메일 발송에 사용할 프로모션 데이터 객체
     * @return 이메일 전송이 시작되었음을 나타내는 "ok" 문자열.
     * @throws RuntimeException 이메일 전송 중 예외가 발생한 경우.
     */

    ResponseEntity<String> sendMailPromo(PromotionsDTO promotionsDTO);

    /**
     * 비밀번호 재설정 이메일을 특정 사용자에게 비동기적으로 발송합니다.
     * <p>
     * 이 메서드는 주어진 이메일 주소에 대해 비밀번호 재설정 이메일을 비동기적으로 발송합니다.
     * 이메일 전송이 성공하면 콘솔에 서비스 시간을 출력하고, 실패하면 로그에 오류를 기록하고 RuntimeException을 발생시킵니다.
     * 메서드 호출 후에는 "ok" 문자열을 반환하여 전송이 시작되었음을 나타냅니다.
     *
     * @param userDTO 발송할 이메일 메시지 객체.
     * @return 이메일 전송이 시작되었음을 나타내는 "ok" 문자열.
     * @throws RuntimeException 이메일 전송 중 예외가 발생한 경우.
     */
    String sendMailPWReset(UserDTO userDTO);


}
