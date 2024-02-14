package com.lumen.www.dao;

import com.lumen.www.dto.auth.RefreshToken;

import java.util.Date;
import java.util.Optional;

public interface TokenRepository {

    /**
     * 사용자의 새로운 리프레시 토큰과 해당 토큰의 만료 날짜를 저장합니다.
     * <p>
     * 이 메서드는 주어진 사용자 이름에 대해 리프레시 토큰을 저장하며,
     * 토큰이 만료되는 날짜와 시간도 함께 저장합니다. 저장된 토큰은
     * 사용자가 시스템에 다시 접속할 때 인증을 유지하는 데 사용될 수 있습니다.
     *
     * @param userName 사용자 이름을 나타내는 {@code String}. 이 값은 해당 사용자를 식별하는 데 사용됩니다.
     * @param refreshToken 사용자에게 할당된 새로운 리프레시 토큰을 나타내는 {@code String}.
     * @param expiryDate 토큰의 만료 날짜를 나타내는 {@code Date}. 이 날짜가 지나면 토큰은 더 이상 유효하지 않습니다.
     */

    void saveRefreshToken(String userName, String refreshToken, Date expiryDate);

    /**
     * 관리자에게 할당된 리프레시 토큰을 삭제합니다.
     * <p>
     * 이 메서드는 시스템 내에서 특정 관리자 ID에 연결된 리프레시 토큰을
     * 찾아 해당 토큰을 삭제하는 기능을 수행합니다. 토큰 삭제 작업은 보안 상의 이유로
     * 필요할 때 수행되며, 관리자의 접근 권한 변경이나 토큰이 더 이상 안전하지 않다고
     * 판단될 때 사용됩니다.
     *
     * @param adminId 리프레시 토큰을 삭제할 관리자의 ID를 나타내는 {@code String}.
     *                이 값은 시스템에서 관리자를 유일하게 식별하는 데 사용됩니다.
     */

    void deleteRefreshToken(String adminId);

    /**
     * 제공된 리프레시 토큰 문자열을 검증하고, 해당 토큰 정보를 조회합니다.
     * <p>
     * 이 메서드는 시스템에 저장된 리프레시 토큰 중에서 입력받은 토큰 문자열과 일치하는
     * 토큰이 있는지 확인합니다. 유효한 토큰이 존재할 경우, 해당 토큰 정보를 담고 있는
     * {@code RefreshToken} 객체를 반환합니다. 반환되는 객체는 {@code Optional}로
     * 감싸져 있어, 토큰이 존재하지 않거나 유효하지 않은 경우 비어 있는 {@code Optional}을
     * 반환하여 토큰의 부재를 안전하게 처리할 수 있습니다.
     *
     * @param refreshToken 검증하고자 하는 리프레시 토큰의 문자열을 나타내는 {@code String}.
     * @return 해당 리프레시 토큰에 대응하는 {@code RefreshToken} 객체를 담고 있는 {@code Optional}.
     *         토큰이 유효하고 존재할 경우 해당 객체를 반환하며, 그렇지 않을 경우 비어 있는 {@code Optional}을 반환합니다.
     */

    Optional<RefreshToken> refreshTokenCK(String refreshToken);


}
