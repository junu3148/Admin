package com.lumen.www.dao;

import org.springframework.stereotype.Repository;
import com.lumen.www.vo.AdminUser;
import com.lumen.www.vo.CurrentSituationVO;
import com.lumen.www.vo.MonthVO;
import com.lumen.www.vo.QnaVO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Repository
public class MemoryAdminRepository implements AdminRepository {

    // 1차 로그인
    @Override
    public AdminUser adminLogin(AdminUser adminUser) {
        System.out.println("adminLogin MemoryAdminRepository()");

        if ("test".equals(adminUser.getAdminId()) && "123".equals(adminUser.getAdminPassword())) {
            AdminUser adminUserDB = new AdminUser();
            adminUserDB.setAdminId("test1");
            adminUserDB.setAdminPassword("123");
            adminUserDB.setRole(0);
            return adminUserDB;
        }
        return null;
    }

    // 2차 로그인
    @Override
    public AdminUser adminLoginCk(AdminUser adminUser) {
        System.out.println("adminLoginCk MemoryAdminRepository()");

        if ("김동규".equals(adminUser.getAdminName())) {
            AdminUser adminUserDB = new AdminUser();
            adminUserDB.setAdminId("test");
            //adminUserDB.setPassword("123");
            adminUserDB.setRole(1);
            adminUserDB.setAdminName("김동규");
            //adminUserDB.setAccountName("신한은행");
            //adminUserDB.setAccountNumber("111");
            return adminUserDB;
        }
        return null;
    }

    // 2차로그인
    @Override
    public AdminUser adminLoginCk(String userName) {
        System.out.println("adminLoginCk MemoryAdminRepository()");

        if ("임정민".equals(userName)) {
            AdminUser adminUserDB = new AdminUser();
            adminUserDB.setAdminName("임정민");
            adminUserDB.setAdminId("test");
            //adminUserDB.setPassword("123");
            adminUserDB.setRole(0);
            return adminUserDB;
        }
        return null;
    }

    // 가입자 현황
    @Override
    public int subscriberCount() {
        System.out.println("subscriberCount MemoryAdminRepository()");

        // Random 객체 생성
        Random random = new Random();
        return random.nextInt(200) + 1;
    }

    // 메인페이지 월별가입자 그래프
    @Override
    public MonthVO getMonthlySalesChart() {
        System.out.println("monthlySalesChart MemoryAdminRepository()");

        String[] months = {"2023년 2월", "2023년 3월", "2023년 4월", "2023년 5월", "2023년 6월", "2023년 7월", "2023년 8월", "2023년 9월", "2023년 10월", "2023년 11월", "2023년 12월", "2024년 1월"};
        Integer[] subscribersCount = {100, 200, 200, 157, 119, 229, 351, 192, 191, 220, 213, 317};

        MonthVO monthVO = new MonthVO();
        monthVO.setMonth(Arrays.asList(months));
        monthVO.setSubscribersCount(Arrays.asList(subscribersCount));

        return monthVO;
    }

    // 메인페이지 현황지표
    @Override
    public List<CurrentSituationVO> getCurrentSituation() {
        System.out.println("currentSituation MemoryAdminRepository()");

        int[] userCounts = {124, 332, 123000, 2, 2};
        String[] upDowns = {"up", "up", "up", "down", "down"};
        String[] percentages = {"4%", "6%", "7%", "10%", "10%"};

        List<CurrentSituationVO> currentSituationVOs = new ArrayList<>();

        for (int i = 0; i < userCounts.length; i++) {
            CurrentSituationVO currentSituationVO = new CurrentSituationVO();
            currentSituationVO.setUserCount(userCounts[i]);
            currentSituationVO.setUpDown(upDowns[i]);
            currentSituationVO.setPercentage(percentages[i]);
            currentSituationVOs.add(currentSituationVO);
        }
        return currentSituationVOs;
    }

    // 메인페이지 문의현황
    @Override
    public List<QnaVO> getMainInquiryList() {
        System.out.println("mainInquiryList MemoryAdminRepository()");

        String[] inquiryDate = {"2024.01.01", "2024.01.02", "2024.01.03", "2024.01.04", "2024.01.05", "2024.01.06"};
        String[] inquiryDetails = {"로그인이 안돼요1", "로그인이 안돼요2", "로그인이 안돼요3", "로그인이 안돼요4", "로그인이 안돼요5", "로그인이 안돼요6"};
        String[] answerStatus = {"답변대기", "답변대기", "답변대기", "답변대기", "답변대기", "답변완료"};
        String[] subscriptionPlan = {"pro", "pro", "pro", "pro", "pro", "basic"};

        List<QnaVO> qnaVOS = new ArrayList<>();

        for (int i = 0; i < inquiryDate.length; i++) {
            QnaVO qnaVO = new QnaVO();
            qnaVO.setInquiryDate(inquiryDate[i]);
            qnaVO.setInquiryDetails(inquiryDetails[i]);
            qnaVO.setAnswerStatus(answerStatus[i]);
            qnaVO.setSubscriptionPlan(subscriptionPlan[i]);
            qnaVOS.add(qnaVO);
        }
        return qnaVOS;
    }
}