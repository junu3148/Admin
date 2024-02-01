package com.lumen.www.dto;


import lombok.Data;

@Data
public class SearchDTO {

    private String userName;
    private String planName;
    private String startDate;
    private String endDate;
    private String keyword;
    private String searchType;

}
