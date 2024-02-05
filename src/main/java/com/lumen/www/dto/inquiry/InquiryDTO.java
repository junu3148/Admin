package com.lumen.www.dto.inquiry;

import lombok.Data;

@Data
public class InquiryDTO {

    private Long inquiryKey; // PK
    private String inquiryDate;
    private String inquiryContent;
    private String inquiryStatus;
    private String qna;
    private String inquiryType;
    private String planName;
    private String planType;

    // FK
    private String userKey;
    private String userName;
    private String adminKey;

    // 이건 삭제예정
    private String subscriptionPlan;



}