package com.yxx.mall.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xyong
 * date 2021-06-25
 */
@Configuration
public class MallElasticsearchConfig {

    @Value("${es.hostname}")
    private String hostName;
    @Value("${es.port}")
    private Integer port;
    @Value("${es.scheme}")
    private String scheme;

    public static final RequestOptions COMMON_OPTIONS;
    static {
        RequestOptions.Builder builder=RequestOptions.DEFAULT.toBuilder();
        COMMON_OPTIONS=builder.build();
    }

    @Bean
    public RestHighLevelClient esRestClient() {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(hostName, port, scheme)));
        return client;
    }
}
