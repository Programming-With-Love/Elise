package com.hnqc.ironhand.schedule.task;

import com.hnqc.ironhand.common.pojo.message.TaskMessage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

public class TaskListener {
    private String groupId = "task";
    private String topic = "task";

    @KafkaListener(topics = "#{__listener.topic}", groupId = "#{__listener.groupId}")
    public void listen(ConsumerRecord<Integer, TaskMessage> record) {

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
