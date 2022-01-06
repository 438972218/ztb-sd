package com.xdcplus.statement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableFeignClients
@EnableEurekaClient
@EnableTransactionManagement
@MapperScan("com.xdcplus.statement.mapper")
@EnableAsync
@EnableCaching
@EnableScheduling
@EnableDiscoveryClient
@SpringBootApplication
public class StatementApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatementApplication.class, args);
    }

}
