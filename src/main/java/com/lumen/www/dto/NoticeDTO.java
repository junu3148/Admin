package com.lumen.www.dto;

import lombok.Data;

@Data
public class NoticeDTO {

    private Long noticeKey;
    private String noticeTitle;
    private String noticeContent;
    private String noticeDate;
    private int noticeHits;

    private Long adminKey;
    private String adminId;

}
