package com.hnqc.ironhand.common.broadcast;

import com.hnqc.ironhand.common.pojo.message.BroadcastMessage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

public class BroadcastListener {
    private String groupId;
    private String topic = "broadcast";
    private Handler handler;

    public interface Handler {
        void handle(BroadcastMessage message);
    }

    public BroadcastListener(String groupId, Handler handler) {
        this.groupId = groupId;
        this.handler = handler;
    }

    @KafkaListener(topics = "#{__listener.topic}", groupId = "#{__listener.groupId}")
    public void listen(ConsumerRecord<Integer, BroadcastMessage> record) {
        this.handler.handle(record.value());
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
