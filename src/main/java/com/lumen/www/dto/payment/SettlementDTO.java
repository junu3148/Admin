package com.lumen.www.dto.payment;

import lombok.Data;

@Data
public class SettlementDTO {

    private String type;
    private int year;
    private int month;
    private int start;
    private int end;




}
