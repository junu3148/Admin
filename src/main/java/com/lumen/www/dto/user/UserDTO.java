package com.lumen.www.dto.user;

import lombok.Data;

@Data
public class UserDTO {

    private Long userKey;
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
    private String  outAmount;
    private String notInfo;
    private int subRound;
    private String notSubRound;
    private String company;
    private int searchType;


    private Long planKey;
    private String planName;
    private String planType;
    private int planPrice;

    private Long subKey;


    


}
