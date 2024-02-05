package com.lumen.www.dto.pricing;

import lombok.Data;

@Data
public class PriceSearchDTO {

    private String userName;
    private String planName;
    private String outInfo;
    private int minNum;
    private int maxNum;

}
