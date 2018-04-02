package com.hnqc.ironhand.common;

import com.hnqc.ironhand.utils.CommonConfig;
import org.springframework.beans.factory.annotation.Configurable;
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
}
