package com.lumen.www.dto;

import lombok.Data;

@Data
public class NoticeListDTO {

    private Long noticeKey;
    private int 번호;
    private String 제목;
    private String 등록일자;
    private int 조회수;
}
