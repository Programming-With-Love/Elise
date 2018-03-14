package com.hnqc.ironhand.common.message;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class BroadcastListener {
    @KafkaListener
    public void listen(ConsumerRecord<String,String> cr){
        System.out.println(cr.value());
    }
}
