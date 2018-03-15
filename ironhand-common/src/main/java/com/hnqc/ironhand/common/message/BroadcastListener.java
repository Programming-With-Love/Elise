package com.hnqc.ironhand.common.message;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.CountDownLatch;

public class BroadcastListener {
    private String groupId;
    private String topic = "broadcast";
    public final static CountDownLatch latch = new CountDownLatch(1);

    public BroadcastListener(String groupId) {
        this.groupId = groupId;
    }

    @KafkaListener(topics = "#{__listener.topic}", groupId = "#{__listener.groupId}")
    public void listen(ConsumerRecord<String, String> record) {
        System.out.println(groupId+"组收到："+record.value());
        latch.countDown();
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
