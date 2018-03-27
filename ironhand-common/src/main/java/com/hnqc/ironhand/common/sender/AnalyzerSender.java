package com.hnqc.ironhand.common.sender;

import com.hnqc.ironhand.common.pojo.entity.Seed;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class AnalyzerSender {
    private KafkaTemplate<Integer, Object> template;

    public AnalyzerSender(KafkaTemplate<Integer, Object> template) {
        this.template = template;
    }

    public void send(Seed message) {
        template.send("analyzer", message);
    }
}
