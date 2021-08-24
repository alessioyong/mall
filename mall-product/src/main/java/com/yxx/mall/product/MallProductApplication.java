package com.yxx.mall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
@EnableCaching
@SpringBootApplication
@EnableFeignClients("com.yxx.mall.product.fegin")
@MapperScan("com.yxx.mall.product.mapper")
public class MallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallProductApplication.class, args);
        System.out.println("===========================\n"+
                           "===     商品服务启动成功   ===\n"+
                           "===========================\n");
    }

}
