package com.yxx.mall.search;

import com.alibaba.fastjson.JSON;
import com.yxx.mall.search.config.MallElasticsearchConfig;
import lombok.Data;
import lombok.ToString;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class MallSearchApplicationTests {

    @Autowired
    RestHighLevelClient client;

    @Test
    public void searchData() throws IOException{
        //1.创建检索条件
        SearchRequest searchRequest = new SearchRequest();
        //指定索引
        searchRequest.indices("bank");
        //指定DSL，检索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchQuery("address","mill"));

        //1.1)按照年龄的值进行聚合
        TermsAggregationBuilder ageAgg = AggregationBuilders.terms("ageAgg").field("age").size(10);
        sourceBuilder.aggregation(ageAgg);
        //1.2）计算平均薪资
        AvgAggregationBuilder balanceAvg = AggregationBuilders.avg("balanceAvg").field("balance");
        sourceBuilder.aggregation(balanceAvg);

        System.out.println(sourceBuilder.toString());
        searchRequest.source(sourceBuilder);
        //2.执行检索
        SearchResponse searchResponse = client.search(searchRequest, MallElasticsearchConfig.COMMON_OPTIONS);
        //3,分析结果
        System.out.println(searchResponse.toString());

        //获取所有的命中纪录
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit:searchHits){
            String sourceAsString = hit.getSourceAsString();
            Account account = JSON.parseObject(sourceAsString, Account.class);
            System.out.println(account);
        }

        Aggregations aggregations = searchResponse.getAggregations();
        Terms ageAgg1 = aggregations.get("ageAgg");
        for (Terms.Bucket bucket : ageAgg1.getBuckets()) {
            String keyAsString = bucket.getKeyAsString();
            System.out.println("年龄："+keyAsString+"==>"+bucket.getDocCount());
        }

        Avg balanceAvg1 = aggregations.get("balanceAvg");
        System.out.println("平均薪资："+balanceAvg1.getValue());
    }

    @Data
    @ToString
    static class Account{
        private int account_number;
        private int balance;
        private String firstname;
        private String lastname;
        private int age;
        private String gender;
        private String address;
        private String employer;
        private String email;
        private String city;
        private String state;
    }
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
