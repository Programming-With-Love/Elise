package com.hnqc.ironhand.common;

import com.hnqc.ironhand.spider.Spider;
import com.hnqc.ironhand.spider.downloader.HttpClientDownloader;
import com.hnqc.ironhand.spider.message.CommunicationManager;
import com.hnqc.ironhand.spider.pipeline.MysqlPipeline;
import com.hnqc.ironhand.spider.processor.ExtractorPageProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class CommonApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommonApplication.class);
    }

    @Bean
    @ConfigurationProperties(prefix = "com.hnqc.config")
    public CommonConfig getConfig() {
        return new CommonConfig();
    }

    @Bean
    public SpringKafkaCommunicationManager manager() {
        return new SpringKafkaCommunicationManager();
    }

    @Bean
    public Spider spider(SpringKafkaCommunicationManager manager, DistributedScheduler scheduler) {
        return new Spider(manager,
                null,
                null,
                scheduler);
    }
}
