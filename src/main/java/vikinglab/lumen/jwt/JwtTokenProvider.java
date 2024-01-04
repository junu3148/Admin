package vikinglab.lumen.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vikinglab.lumen.vo.AdminUser;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class JwtTokenProvider {

    private final Key key;
    public final Long ACCESS_TOKEN_EXPIRE_COUNT = 30 * 60 * 1000L; // 30분

    // application.yml에서 secret 값 가져와서 key에 저장
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(AdminUser adminUser) {

        //Header 부분 설정
        Map<String, Object> headers = new HashMap<>();
        // 토큰의 유형
        headers.put("typ", "JWT");
        // 서명 알고리즘
        headers.put("alg", "HS256");

        //payload 부분 설정
        Map<String, Object> payloads = new HashMap<>();

        payloads.put("adminUserId", adminUser.getId());
        payloads.put("isAdmmin", adminUser.getIsAdmin());
        payloads.put("userName", adminUser.getUserName());

        long now = (new Date()).getTime();

        // 토큰 Builder
        return Jwts.builder()
                .setHeader(headers) // Headers 설정
                .setClaims(payloads) // Claims 설정
                .setSubject("AdminUser") // 토큰 용도(제목)
                .setExpiration(new Date(now + ACCESS_TOKEN_EXPIRE_COUNT)) // 토큰 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS256) // HS256과 Key로 Sign
                .compact(); // 토큰 생성
    }

}