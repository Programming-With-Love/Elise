package com.hnqc.ironhand.common;

import com.hnqc.ironhand.common.pojo.Seed;
import com.hnqc.ironhand.spider.Page;
import com.hnqc.ironhand.spider.Request;
import com.hnqc.ironhand.spider.Task;
import com.hnqc.ironhand.spider.message.CommunicationManager;
import com.hnqc.ironhand.spider.message.ThreadCommunicationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * Spring Kafka Communication Manager
 *
 * @author zido
 * @date 2018/04/19
 */
public class SpringKafkaCommunicationManager implements CommunicationManager {
    private KafkaTemplate<Integer, Object> template;

    private ThreadCommunicationManager threadCommunicationManager;

    public SpringKafkaCommunicationManager() {

    }

    public SpringKafkaCommunicationManager(ThreadCommunicationManager communicationManager) {
        this.threadCommunicationManager = communicationManager;
    }

    @Override
    public void registerAnalyzer(AnalyzerListener listener) {
        if (threadCommunicationManager != null) {
            threadCommunicationManager.registerAnalyzer(listener);
        } else {

        }
    }

    @Override
    public void registerDownloader(DownloadListener listener) {
        if (threadCommunicationManager != null) {
            threadCommunicationManager.registerDownloader(listener);
        } else {

        }
    }

    @Override
    public void removeAnalyzer(AnalyzerListener listener) {
        if (threadCommunicationManager != null) {
            threadCommunicationManager.registerAnalyzer(listener);
        }
    }

    @Override
    public void removeDownloader(DownloadListener listener) {
        if (threadCommunicationManager != null) {
            threadCommunicationManager.registerDownloader(listener);
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void process(Task task, Request request, Page page) {
        template.send("analyzer", new Seed().setTask(task).setRequest(request).setPage(page));
    }

    @Override
    public void download(Task task, Request request) {
        template.send("download", new Seed().setTask(task).setRequest(request));
    }

    @Override
    public int blockSize() {
        return 0;
    }

    @Override
    public void stop() {

    }

    @Override
    public Status getStatus() {
        return null;
    }

    @Autowired
    public SpringKafkaCommunicationManager setTemplate(KafkaTemplate<Integer, Object> template) {
        this.template = template;
        return this;
    }
}
