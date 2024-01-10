package com.lumen.www.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MonthlySubscriberDTO {
    private List<String> month ;
    private List<Integer> subscribersCount;
}
