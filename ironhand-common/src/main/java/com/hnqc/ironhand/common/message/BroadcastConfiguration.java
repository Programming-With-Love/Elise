package com.hnqc.ironhand.common.message;

import com.hnqc.ironhand.common.CommonConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BroadcastConfiguration {

    private CommonConfig commonConfig;

    @Autowired
    public BroadcastConfiguration(CommonConfig commonConfig) {
        this.commonConfig = commonConfig;
    }

    @Bean("broadcastListener")
    public BroadcastListener listener() {
        return new BroadcastListener(commonConfig.getBroadId());
    }
}
