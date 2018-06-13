package site.zido.elise.schedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import site.zido.elise.DistributedTask;
import site.zido.elise.Request;
import site.zido.elise.Spider;
import site.zido.elise.Task;
import site.zido.elise.common.SimpleRedisDuplicationProcessor;
import site.zido.elise.common.SpringKafkaTaskScheduler;
import site.zido.elise.scheduler.DuplicationProcessor;
import site.zido.elise.scheduler.NoDepuplicationProcessor;
import site.zido.elise.scheduler.SimpleTaskScheduler;
import site.zido.elise.scheduler.TaskScheduler;

import java.io.IOException;

/**
 * ScheduleClient
 *
 * @author zido
 */
public class ScheduleClient {
    private Spider spider;
    private ObjectMapper mapper = new ObjectMapper();
    private DuplicationProcessor duplicationProcessor;
    private TaskScheduler scheduler;

    public ScheduleClient(String kafkaServers, String redisUrl, String groupId, String topicAnalyzer, String topicDownloader) {
        this.duplicationProcessor = new SimpleRedisDuplicationProcessor(redisUrl);
        this.scheduler = new SpringKafkaTaskScheduler(new SimpleTaskScheduler(new NoDepuplicationProcessor()).setPoolSize(2), duplicationProcessor)
                .setBootstrapServers(kafkaServers)
                .setGroupId(groupId)
                .setTopicDownload(topicDownloader)
                .setTopicAnalyzer(topicAnalyzer);
        this.spider = new Spider(scheduler);
    }

    public void start() {
        spider.start();
    }

    public void stop() {
        spider.stop();
    }

    public void pushRequest(DistributedTask task, Request request) {
        spider.pushRequest(task, request);
    }

    public void pushFromJson(String json) {
        try {
            JsonRequest jsonRequest = mapper.readValue(json, JsonRequest.class);
            pushRequest(jsonRequest.getTask(), jsonRequest.getRequest());
        } catch (IOException e) {
            throw new RuntimeException("read from json error", e);
        }
    }

    public void clearDuplications(Task task) {
        duplicationProcessor.resetDuplicateCheck(task);
    }
}
