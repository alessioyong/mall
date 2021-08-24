package com.yxx.mall.product;

import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MallProductApplicationTests {

    @Autowired
    RedissonClient redissonClient;
    @Test
    void contextLoads() {
    }

    @Test
    public void redisson(){
        System.out.println(redissonClient);
    }

}
