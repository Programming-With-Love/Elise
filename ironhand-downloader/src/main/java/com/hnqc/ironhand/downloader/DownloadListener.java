package com.hnqc.ironhand.downloader;

import com.hnqc.ironhand.common.pojo.SeedData;
import com.hnqc.ironhand.common.pojo.UrlEntry;
import com.hnqc.ironhand.common.pojo.entity.Seed;
import com.hnqc.ironhand.common.utils.ValidateUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DownloadListener {
    private String groupId = "download";
    private String topic = "download";

    @KafkaListener(topics = "#{__listener.topic}", groupId = "#{__listener.groupId}")
    public void listen(ConsumerRecord<Integer, Seed> record) {
        Seed seed = record.value();
        SeedData data = seed.getData();
        List<UrlEntry> html = data.getHtml();
        for (UrlEntry urlEntry : html) {
            String name = urlEntry.getName();
            String value = urlEntry.getValue();
            if(ValidateUtils.isEmpty(value)){

            }
        }
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
