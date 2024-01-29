package com.lumen.www.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
public class UserDTO {

    private int userKey;
    private String userId;
    private String userPassword;
    private String userName;
    private String phoneNumber;
    private String accessionDate;
    private String withdrawalDate;
    private int birthYear;
    private String age;
    private String occupation;
    private String country;
    private String gender;
    private String emailAccept;
    private String promoAccept;
    private String userStatus;
    private int outInfo;
    private int subRound;
    private String company;

    private int planKey;
    private String planName;
    private String planType;
    private int planPrice;



}
