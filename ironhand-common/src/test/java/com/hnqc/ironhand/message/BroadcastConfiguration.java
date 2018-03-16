package com.hnqc.ironhand.message;

import com.hnqc.ironhand.common.broadcast.BroadcastListener;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class BroadcastConfiguration {
    @Bean("broadcastListener_test_1")
    public BroadcastListener listener() {
        return new BroadcastListener("receive2", new BroadcastTest.TestHandler());
    }

    @Bean
    public BroadcastListener.Handler handler() {
        return new BroadcastTest.TestHandler();
    }
}
