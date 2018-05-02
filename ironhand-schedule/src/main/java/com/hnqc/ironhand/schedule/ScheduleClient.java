package com.hnqc.ironhand.schedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hnqc.ironhand.DistributedTask;
import com.hnqc.ironhand.Request;
import com.hnqc.ironhand.Spider;
import com.hnqc.ironhand.Task;
import com.hnqc.ironhand.common.SimpleRedisDuplicationProcessor;
import com.hnqc.ironhand.common.SpringKafkaTaskScheduler;
import com.hnqc.ironhand.scheduler.DuplicationProcessor;
import com.hnqc.ironhand.scheduler.NoDepuplicationProcessor;
import com.hnqc.ironhand.scheduler.SimpleTaskScheduler;
import com.hnqc.ironhand.scheduler.TaskScheduler;

import java.io.IOException;

/**
 * ScheduleClient
 *
 * @author zido
 * @date 2018/04/23
 */
public class ScheduleClient {
    private Spider spider;
    private ObjectMapper mapper = new ObjectMapper();
    private DuplicationProcessor duplicationProcessor;
    private TaskScheduler scheduler;

    public ScheduleClient(String kafkaServers, String redisUrl) {
        this.duplicationProcessor = new SimpleRedisDuplicationProcessor(redisUrl);
        this.scheduler = new SpringKafkaTaskScheduler(new SimpleTaskScheduler(new NoDepuplicationProcessor()).setPoolSize(2), duplicationProcessor).setBootstrapServers(kafkaServers);
        this.spider = new Spider(scheduler);
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

    public void clearDuplications(Task task) {
        duplicationProcessor.resetDuplicateCheck(task);
    }
}
