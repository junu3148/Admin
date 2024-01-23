package com.lumen.www.dao;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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


}
