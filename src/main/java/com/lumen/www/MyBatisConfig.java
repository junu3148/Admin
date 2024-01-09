package com.lumen.www;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.lumen.www.mapper")
public class MyBatisConfig {

}
