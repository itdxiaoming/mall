package com.worryfree.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.worryfree.search.feign")
@EnableEurekaClient
public class SearchWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchWebApplication.class,args);
    }
}
