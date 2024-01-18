package com.lumen.www.dto;


import lombok.Data;

@Data
public class JoinListDTO {
    private int userKey;
    private String 고객명;
    private String 이메일;
    private String 연락처;
    private String 플랜;
    private String 가입일;
    private String 회원탈퇴일;
    private String 직업;
    private String 연령;
    private String 성별;
    private String 국가;


}
