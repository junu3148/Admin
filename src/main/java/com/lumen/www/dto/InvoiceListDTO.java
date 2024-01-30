package com.lumen.www.dto;

import lombok.Data;

@Data
public class InvoiceListDTO {

    private int invoiceKey;
    private String 고객명;
    private String 이메일;
    private String 연락처;
    private String 플랜;
    private String 플랜구분;
    private String 결제일;
    private String 인보이스;
    private int 결제금액;

}
