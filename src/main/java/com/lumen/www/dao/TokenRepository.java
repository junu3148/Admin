package com.lumen.www.dao;

import com.lumen.www.dto.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TokenRepository {

    private final SqlSession sqlSession;

    // 토근 저장
    public void saveRefreshToken(String userName, String refreshToken, Date expiryDate) {
        sqlSession.insert("token.saveRefreshToken", Map.of("userName", userName, "refreshToken", refreshToken, "expiryDate", expiryDate));
    }

    // 데이터베이스에서 사용자 이름에 해당하는 리프레시 토큰을 조회
    public Optional<RefreshToken> findRefreshToken(String userName) {
        return Optional.ofNullable(sqlSession.selectOne("token.findRefreshToken", userName));
    }

    // 데이터베이스의 리플레시 토큰 유효 확인. 파라미터가 따로 관리;
    public Optional<RefreshToken> refreshTokenCK(String refreshToken){
        return  Optional.ofNullable(sqlSession.selectOne("token.refreshTokenCK", refreshToken));
    }

}
