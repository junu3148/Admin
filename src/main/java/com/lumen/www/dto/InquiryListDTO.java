package com.lumen.www.dto;

import lombok.Data;

@Data
public class InquiryListDTO {

    private Long inquiryKey; // PK
    private String 번호;
    private String 유형;
    private String 이름;
    private String 이메일;
    private String 플랜;
    private String 플랜구분;
    private String 제목;
    private String 문의일자;
    private String 처리상태;



}