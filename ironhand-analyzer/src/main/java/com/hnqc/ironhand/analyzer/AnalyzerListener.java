package com.hnqc.ironhand.analyzer;

import com.hnqc.ironhand.common.AbstractDistributedScheduler;
import com.hnqc.ironhand.common.pipelines.SavedPipeline;
import com.hnqc.ironhand.common.pojo.Seed;
import com.hnqc.ironhand.common.service.impl.AbstractAsyncDownloader;
import com.hnqc.ironhand.spider.distributed.DsSpiderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AnalyzerListener {
    private String groupId = "analyzer";
    private String topic = "analyzer";
    private SavedPipeline savedPipeline;
    private AbstractDistributedScheduler scheduler;
    private AbstractAsyncDownloader downloader;

    @KafkaListener(topics = "#{__listener.topic}", groupId = "#{__listener.groupId}")
    public void listen(Seed seed) {
        if (seed.getRequest() == null) {
            //初始种子
            DsSpiderImpl dsSpider = seed.getDsSpider();
            dsSpider.addPipeline(savedPipeline);
            dsSpider.setScheduler(scheduler);
            dsSpider.setDownloader(downloader);
            dsSpider.run();
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

    @Autowired
    public void setScheduler(AbstractDistributedScheduler scheduler) {
        this.scheduler = scheduler;
    }


    @Autowired
    public void setDownloader(AbstractAsyncDownloader downloader) {
        this.downloader = downloader;
    }

    @Autowired
    public void setSavedPipeline(SavedPipeline savedPipeline) {
        this.savedPipeline = savedPipeline;
    }
}
