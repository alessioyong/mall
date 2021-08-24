package com.yxx.mall.product.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author xyong
 * date 2021-08-24
 */
@Configuration
public class MyRedissonConfig {

    /**
     * 所有对Redisson的使用都是通过RedissonClient对象
     * @return
     * @throws IOException
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() throws IOException {
        //1.创建配置
        //Redis url should start with redis:// or rediss:// (for SSL connection)
        Config config = new Config();
        config.useSingleServer().setAddress("redis://139.9.65.255:6379");
        //2.根据config对象船建初RedissonClien实例
        return Redisson.create(config);
    }
}
