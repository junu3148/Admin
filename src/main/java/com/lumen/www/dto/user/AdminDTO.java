package com.lumen.www.dto.user;

import lombok.Data;

@Data
public class AdminDTO {

    private Long adminKey;
    private String adminName;
    private String depositor;
    private String bankName;
    private String accountNumber;
    private int role;

}
