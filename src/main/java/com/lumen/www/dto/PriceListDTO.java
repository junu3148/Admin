package com.lumen.www.dto;

import lombok.Data;

@Data
public class PriceListDTO {

    private Long userKey;
    private String 고객명;
    private String 이메일;
    private String 연락처;
    private String 플랜;
    private String 플랜구분;
    private String 미결제금액;
    private String 회차;
    private String 미결제안내;
    private String 활동정지;

}
