package site.zido.elise.support.redis.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.zido.elise.distributed.AbstractQueueScheduler;
import site.zido.elise.http.Request;
import site.zido.elise.scheduler.AbstractScheduler;
import site.zido.elise.scheduler.Seed;
import site.zido.elise.task.Task;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 使用redis实现的任务调度器
 *
 * @author zido
 */
public class RedisTaskScheduler extends AbstractQueueScheduler implements Runnable {
    private static Logger LOGGER = LoggerFactory.getLogger(RedisTaskScheduler.class);
    private RedisClient redisClient;
    private StatefulRedisConnection<String, String> connection;
    private String key = "elise:seed:queue";
    private AtomicBoolean STATE = new AtomicBoolean(false);
    private ObjectMapper mapper;

    /**
     * redis url like "redis://password@localhost:6379/0"
     *
     * @param url redis server url
     */
    public RedisTaskScheduler(String url) {
        redisClient = RedisClient.create(url);
    }

    @Override
    public void run() {
        preStart();
        //TODO 工作密取获取种子进行任务下载
    }

    /**
     * Pre start.
     */
    private void preStart() {
        if (STATE.compareAndSet(false, true)) {
            connection = redisClient.connect();
        }
    }

    @Override
    protected void pushWhenNoDuplicate(Task task, Request request) {
        preStart();
        RedisCommands<String, String> syncCommands = connection.sync();
        try {
            String value = mapper.writeValueAsString(new Seed(task, request));
            syncCommands.rpush(key, value);
        } catch (JsonProcessingException e) {
            LOGGER.error("serialize seed to json error", e);
        }
    }

    @Override
    public void cancel(boolean ifRunning) {
        connection.close();
        redisClient.shutdown();
    }
}
