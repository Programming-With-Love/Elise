package com.hnqc.ironhand.common.sender;

import com.hnqc.ironhand.common.pojo.message.TaskMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TaskSender {
    private KafkaTemplate<Integer, Object> template;

    public TaskSender(KafkaTemplate<Integer, Object> template) {
        this.template = template;
    }

    public void send(TaskMessage message) {
        template.send("task", message);
    }
}
