package com.hnqc.ironhand.common;

import com.hnqc.ironhand.common.pojo.Seed;
import com.hnqc.ironhand.Spider;
import com.hnqc.ironhand.message.CommunicationManager;
import com.hnqc.ironhand.processor.ExtractorPageProcessor;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * 分析模块消息监听器
 *
 * @author zido
 * @date 2018/04/15
 */
public class KafkaAnalyzerListener {
    private String groupId = "analyzer";
    private String topic = "analyzer";
    private CommunicationManager manager;

    public KafkaAnalyzerListener(Spider spider, CommunicationManager manager) {
        spider.setPageProcessor(new ExtractorPageProcessor());
    }

    @KafkaListener(topics = "#{__listener.topic}", groupId = "#{__listener.groupId}")
    public void listen(Seed seed) {
        manager.process(seed.getTask(), seed.getRequest(), seed.getPage());
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
