package com.hnqc.ironhand.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfiguration {
    @Bean
    public CommonConfig common() {
        return new CommonConfig();
    }
}
