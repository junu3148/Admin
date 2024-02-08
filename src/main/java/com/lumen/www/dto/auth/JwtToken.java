package com.lumen.www.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtToken{

    private String grantType;
    private String accessToken;
    private String refreshToken;
    private int role;


}