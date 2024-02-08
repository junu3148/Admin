package com.lumen.www.util;

import com.lumen.www.dao.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCleanupTask {
    private final AdminRepository adminRepository; // AdminRepository 데이터베이스 작업을 처리하는 데 사용
    @Scheduled(cron = "0 0 1 * * ?") // 매일 자정에 실행
    public void cleanUpUsers() {
        adminRepository.deleteUsersWithStatusOlderThanOneMonth();
    }
}
