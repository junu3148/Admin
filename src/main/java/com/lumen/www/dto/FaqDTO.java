package com.lumen.www.dto;

import lombok.Data;

@Data
public class FaqDTO {

    private Long faqKey;
    private String faqTitle;
    private String faqContent;
    private String faqType;

    private Long adminKey;
    private String adminId;
}
