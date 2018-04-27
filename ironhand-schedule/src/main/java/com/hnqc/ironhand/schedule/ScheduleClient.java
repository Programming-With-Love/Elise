package com.hnqc.ironhand.schedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hnqc.ironhand.DistributedTask;
import com.hnqc.ironhand.Request;
import com.hnqc.ironhand.Spider;
import com.hnqc.ironhand.Task;
import com.hnqc.ironhand.common.SimpleRedisDuplicationProcessor;
import com.hnqc.ironhand.common.SpringKafkaTaskScheduler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ScheduleClient
 *
 * @author zido
 * @date 2018/04/23
 */
public class ScheduleClient {
    private Spider spider;
    private ObjectMapper mapper = new ObjectMapper();

    public ScheduleClient(String kafkaServers, String redisUrl) {
        this.spider = new Spider(new SpringKafkaTaskScheduler(new SimpleRedisDuplicationProcessor(redisUrl)).setBootstrapServers(kafkaServers));
    }

    public void start() {
        spider.start();
    }

    public void stop() {
        spider.stop();
    }

    public void pushRequest(DistributedTask task, Request request) {
        spider.pushRequest(task, request);
    }

    public void pushFromJson(String json) {
        try {
            JsonRequest jsonRequest = mapper.readValue(json, JsonRequest.class);
            pushRequest(jsonRequest.getTask(), jsonRequest.getRequest());
        } catch (IOException e) {
            throw new RuntimeException("read from json error", e);
        }
    }
}
