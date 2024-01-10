package com.lumen.www.dto;

import lombok.Data;

@Data
public class UserActivityDTO {
    private int id;
    private int userCount;
    private String upDown;
    private String percentage;

}
