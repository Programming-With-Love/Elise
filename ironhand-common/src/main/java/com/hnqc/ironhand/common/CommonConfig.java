package com.hnqc.ironhand.common;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.hnqc.config")
public class CommonConfig {
    private String broadId;

    public String getBroadId() {
        return broadId;
    }

    public void setBroadId(String broadId) {
        this.broadId = broadId;
    }
}
