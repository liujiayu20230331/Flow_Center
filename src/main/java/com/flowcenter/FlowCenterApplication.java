package com.flowcenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.flowcenter.repository.mapper")
public class FlowCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(FlowCenterApplication.class, args);
    }
}
