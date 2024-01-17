package com.lumen.www.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EmailMessage {

    private String to;
    private String subject;
    private String message;

    // 명시적인 기본 생성자 선언이 더 이상 필요하지 않음
}
