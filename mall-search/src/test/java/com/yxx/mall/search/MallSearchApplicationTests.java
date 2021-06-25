package com.yxx.mall.search;

import com.alibaba.fastjson.JSON;
import com.yxx.mall.search.config.MallElasticsearchConfig;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class MallSearchApplicationTests {

    @Autowired
    RestHighLevelClient client;

    @Test
    public void indexData() throws IOException {
        IndexRequest users = new IndexRequest("users");
        users.id("1");
        User user = new User();
        user.setUserName("zhangsan");
        user.setAge(18);
        user.setGender("男");
        String jsonString = JSON.toJSONString(user);
        users.source(jsonString, XContentType.JSON);
        IndexResponse index = client.index(users, MallElasticsearchConfig.COMMON_OPTIONS);
        System.out.println(index);
    }
    @Data
    class User{
        private String userName;
        private String gender;
        private Integer age;
    }
    @Test
    void contextLoads() {
        System.out.println(client);
    }

}
