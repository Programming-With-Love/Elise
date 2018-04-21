package com.hnqc.ironhand.common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hnqc.ironhand.Request;
import com.hnqc.ironhand.Task;
import com.hnqc.ironhand.scheduler.DuplicationProcessor;
import com.hnqc.ironhand.utils.ValidateUtils;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;

/**
 * Redis-based distributed task scheduling,
 *
 * @author zido
 * @date 2018/04/15
 */
public class SimpleRedisDuplicationProcessor implements DuplicationProcessor {
    private StatefulRedisConnection<String, String> connection;
    private final ObjectMapper objectMapper;
    private String queuePrefix = "queue_";

    private String setPrefix = "set_";

    private String itemPrefix = "item_";

    protected boolean checkForAdditionalInfo(Request request) {
        if (request == null) {
            return false;
        }

        if (!request.getHeaders().isEmpty() || !request.getCookies().isEmpty()) {
            return true;
        }

        if (!ValidateUtils.isEmpty(request.getCharset()) || !ValidateUtils.isEmpty(request.getMethod())) {
            return true;
        }

        if (request.getRequestBody() != null) {
            return true;
        }

        return request.getExtras() != null && !request.getExtras().isEmpty();
    }

    protected String getSetKey(Task task) {
        return setPrefix + task.getId();
    }

    protected String getQueueKey(Task task) {
        return queuePrefix + task.getId();
    }

    protected String getItemKey(Task task) {
        return itemPrefix + task.getId();
    }

    public SimpleRedisDuplicationProcessor(String url) {
        this(url, new ObjectMapper());
        this.objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public SimpleRedisDuplicationProcessor(String url, ObjectMapper objectMapper) {
        RedisClient redisClient = RedisClient.create(url);
        this.connection = redisClient.connect();
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean isDuplicate(Request request, Task task) {
        return connection.sync().sadd(getSetKey(task), request.getUrl()) == 0;
    }

    @Override
    public void resetDuplicateCheck(Task task) {
        connection.sync().del(getSetKey(task));
    }

    @Override
    public int getTotalRequestsCount(Task task) {
        return connection.sync().scard(getSetKey(task)).intValue();
    }

    public void setQueuePrefix(String queuePrefix) {
        this.queuePrefix = queuePrefix;
    }

    public void setSetPrefix(String setPrefix) {
        this.setPrefix = setPrefix;
    }

    public void setItemPrefix(String itemPrefix) {
        this.itemPrefix = itemPrefix;
    }
}
