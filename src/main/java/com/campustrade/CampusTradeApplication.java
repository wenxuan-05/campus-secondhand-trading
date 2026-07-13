package com.campustrade;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.campustrade.mapper")
public class CampusTradeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampusTradeApplication.class, args);
    }
}
