package com.lumen.www.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AdminUser {

    private String token;
    private Long adminKey;
    private String adminId;
    private String adminPassword;
    private String adminName;
    private String depositor;
    private String bankName;
    private String accountNumber;
    private int role;

}