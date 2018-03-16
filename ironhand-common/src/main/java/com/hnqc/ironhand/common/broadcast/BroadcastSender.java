package com.hnqc.ironhand.common.broadcast;

import com.hnqc.ironhand.common.pojo.message.BroadcastMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class BroadcastSender {
    private KafkaTemplate<Integer, Object> template;

    @Autowired
    public BroadcastSender(KafkaTemplate<Integer, Object> template) {
        this.template = template;
    }

    public void send(BroadcastMessage message) {
        template.send("broadcast", message);
    }
}
