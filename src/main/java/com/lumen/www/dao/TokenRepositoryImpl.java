package com.lumen.www.dao;

import com.lumen.www.dto.auth.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TokenRepositoryImpl implements TokenRepository {

    private final SqlSession sqlSession;

    // 토근 저장
    @Override
    public void saveRefreshToken(String userName, String refreshToken, Date expiryDate) {
        sqlSession.insert("token.saveRefreshToken", Map.of("userName", userName, "refreshToken", refreshToken, "expiryDate", expiryDate));
    }

    // 데이터베이스에서 사용자 이름으로 리플레시 토큰 삭제
    @Override
    public void deleteRefreshToken(String adminId) {
        sqlSession.delete("token.deleteRefreshToken", adminId);
    }

    // 데이터베이스의 리플레시 토큰 유효 확인.
    @Override
    public Optional<RefreshToken> refreshTokenCK(String refreshToken) {
        return Optional.ofNullable(sqlSession.selectOne("token.refreshTokenCK", refreshToken));
    }

}
