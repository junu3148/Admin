package com.lumen.www.dto.payment;

import lombok.Data;

@Data
public class PayListDTO {
    private Long userKey;
    private String 고객명;
    private String 이메일;
    private String 연락처;
    private String 플랜;
    private String 플랜구분;
    private String 청약철회;
    private String 철회일;

}
