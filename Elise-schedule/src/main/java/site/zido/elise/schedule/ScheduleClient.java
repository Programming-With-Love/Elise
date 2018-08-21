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
public class ScheduleClient extends Spider {
    private ObjectMapper mapper = new ObjectMapper();
    private TaskScheduler scheduler;

    public ScheduleClient(String kafkaServers, String redisUrl, String groupId, String topicAnalyzer, String topicDownloader) {
        super(new SpringKafkaTaskScheduler(new SimpleTaskScheduler(new NoDepuplicationProcessor()).setPoolSize(2),new SimpleRedisDuplicationProcessor(redisUrl))
                .setBootstrapServers(kafkaServers)
                .setGroupId(groupId)
                .setTopicDownload(topicDownloader)
                .setTopicAnalyzer(topicAnalyzer));
    }

    public void pushFromJson(String json) {
        try {
            JsonRequest jsonRequest = mapper.readValue(json, JsonRequest.class);
            pushRequest(jsonRequest.getTask(), jsonRequest.getRequest());
        } catch (IOException e) {
            throw new RuntimeException("read from json error", e);
        }
    }
}
