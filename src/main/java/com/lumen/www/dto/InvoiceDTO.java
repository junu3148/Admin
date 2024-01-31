package com.lumen.www.dto;

import lombok.Data;

@Data
public class InvoiceDTO {

    private Long userKey;
    private String userId;
    private String userName;
    private String phoneNumber;
    private String accessionDate;
    private String planName;
    private String planType;
    private int planPrice;
    private int subRound;
    private String cityProvince;
    private String basicAddress;
    private String detailedAddress;
    private String vatId;
    private String invoiceCode;
    private String statementEmail;

}
