package com.lumen.www.dao;

import com.lumen.www.dto.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class TokenRepository {

    private final SqlSession sqlSession;

    // 토근 저장
    public void saveRefreshToken(String userName, String refreshToken, Date expiryDate) {

        System.out.println(expiryDate);

        Map<String, Object> params = new HashMap<>();
        params.put("userName", userName);
        params.put("refreshToken", refreshToken);
        params.put("expiryDate", expiryDate); // Date 객체를 직접 전달

        sqlSession.insert("token.saveRefreshToken", params);
    }

    // 데이터베이스에서 사용자 이름에 해당하는 리프레시 토큰을 조회
    // 결과가 없으면 Optional.empty() 반환
    public Optional<RefreshToken> findRefreshToken(String userName) {
        return Optional.ofNullable(sqlSession.selectOne("token.findRefreshToken", userName));
    }


    public boolean isTokenValid(String refreshToken, Date expiration) {
        // 현재 시간을 가져옵니다.
        Date now = new Date();

        // 토큰의 만료 시간이 현재 시간보다 이후인지 확인합니다.
        // 만료 시간이 현재 시간보다 이후라면, 토큰은 유효합니다.
        return expiration.after(now);
    }




}
