package com.lumen.www.dto;

import lombok.Data;

import java.util.Date;

@Data
public class InquiryDTO {

    private int inquiryKey; // PK
    private String inquiryDate;
    private String inquiryDetails;
    private String inquiryStatus;
    private String qna;
    private String inquiryType;

    // FK
    private String userKey;
    private String adminKey;

    // 이건 삭제예정
    private String subscriptionPlan;
}