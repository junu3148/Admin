package com.lumen.www.dao;

import com.lumen.www.dto.auth.RefreshToken;

import java.util.Date;
import java.util.Optional;

public interface TokenRepository {

    /**
     * 새로운 리프레시 토큰을 저장하는 메서드입니다.
     *
     * @param userName     리프레시 토큰과 연결된 사용자의 이름
     * @param refreshToken 저장할 리프레시 토큰 값
     * @param expiryDate   리프레시 토큰의 만료 날짜 및 시간
     */
    void saveRefreshToken(String userName, String refreshToken, Date expiryDate);

    /**
     * 특정 사용자의 리프레시 토큰을 삭제하는 메서드입니다.
     *
     * @param adminId 삭제할 사용자의 아이디
     */
    void deleteRefreshToken(String adminId);

    /**
     * 데이터베이스에서 리프레시 토큰을 검증하는 메서드입니다.
     *
     * @param refreshToken 확인할 리프레시 토큰 값
     * @return 주어진 리프레시 토큰이 유효한 경우 해당 토큰을 포함하는 Optional 객체, 그렇지 않으면 비어있는 Optional 객체를 반환합니다.
     */
    Optional<RefreshToken> refreshTokenCK(String refreshToken);


}
