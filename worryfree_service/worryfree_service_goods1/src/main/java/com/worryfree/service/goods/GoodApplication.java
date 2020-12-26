package com.worryfree.service.goods;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = {"com.worryfree.service.goods.dao"})
public class GoodApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoodApplication.class,args);
    }
}
