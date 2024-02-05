package com.lumen.www.dto.main;

import lombok.Data;
import java.util.List;

@Data
public class MonthVO {
private List<String> month ;
private List<Integer> subscribersCount;
}
