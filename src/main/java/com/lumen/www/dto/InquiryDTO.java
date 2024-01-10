package com.lumen.www.dto;

import lombok.Data;

import java.util.Date;

@Data
public class InquiryDTO {
    private int id;
    private String inquiryDate;
    private String inquiryDetails;
    private String answerStatus;
    private String subscriptionPlan;
}