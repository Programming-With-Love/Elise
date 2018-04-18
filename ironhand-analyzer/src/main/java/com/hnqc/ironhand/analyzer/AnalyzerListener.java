package com.hnqc.ironhand.analyzer;

import com.hnqc.ironhand.common.pipelines.SavedPipeline;
import com.hnqc.ironhand.common.pojo.Seed;
import com.hnqc.ironhand.spider.distributed.downloader.MessageDownloader;
import com.hnqc.ironhand.spider.distributed.scheduler.AbstractDistributedScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * 分析模块消息监听器
 *
 * @author zido
 * @date 2018/04/15
 */
@Component
public class AnalyzerListener {
    private String groupId = "analyzer";
    private String topic = "analyzer";
    private SavedPipeline savedPipeline;
    private AbstractDistributedScheduler scheduler;
    private MessageDownloader downloader;

    @KafkaListener(topics = "#{__listener.topic}", groupId = "#{__listener.groupId}")
    public void listen(Seed seed) {

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

    @Autowired
    public void setScheduler(AbstractDistributedScheduler scheduler) {
        this.scheduler = scheduler;
    }


    @Autowired
    public void setDownloader(MessageDownloader downloader) {
        this.downloader = downloader;
    }

    @Autowired
    public void setSavedPipeline(SavedPipeline savedPipeline) {
        this.savedPipeline = savedPipeline;
    }
}
