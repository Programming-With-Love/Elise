package com.hnqc.ironhand.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hnqc.ironhand.Request;
import com.hnqc.ironhand.Task;
import com.hnqc.ironhand.scheduler.DuplicationProcessor;
import com.hnqc.ironhand.utils.ValidateUtils;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.resource.DefaultClientResources;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * Redis-based distributed task scheduling,
 *
 * @author zido
 * @date 2018/04/15
 */
public class SimpleRedisDistributedScheduler extends AbstractDistributedScheduler {
    private StatefulRedisConnection<String, String> connection;
    private final ObjectMapper objectMapper;

    public SimpleRedisDistributedScheduler(String url) {
        RedisClient redisClient = RedisClient.create(url);
        StatefulRedisConnection<String, String> connect = redisClient.connect();
        this(template, new ObjectMapper());
        this.objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public SimpleRedisDistributedScheduler(RedisTemplate<String, String> template, ObjectMapper objectMapper) {

        Assert.notNull(objectMapper, "'objectMapper' must not be null.");
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean isDuplicate(Request request, Task task) {
        RedisTemplate<String, String> template;

        return template.opsForSet().add(getSetKey(task), request.getUrl()) == 0;
    }

    @Override
    public void resetDuplicateCheck(Task task) {
        template.delete(getSetKey(task));
    }

    @Override
    public int blockSize(Task task) {
        return template.opsForList().size(getQueueKey(task)).intValue();
    }

    @Override
    public int getTotalRequestsCount(Task task) {
        return template.opsForSet().size(getSetKey(task)).intValue();
    }

    @Override
    public Request poll(Task task) {
        String url = template.opsForList().leftPop(getQueueKey(task));
        String key = getItemKey(task);
        String value = template.<String, String>opsForHash().get(key, url);
        if (!ValidateUtils.isEmpty(value)) {
            try {
                return objectMapper.readValue(value, Request.class);
            } catch (IOException e) {
                logger.error("can not parse request[{}]", url);
                return new Request(url);
            }
        }
        return new Request(url);
    }

    @Override
    protected void pushWhenNoDuplicate(Request request, Task task) {
        template.opsForList().rightPush(getQueueKey(task), request.getUrl());
        if (checkForAdditionalInfo(request)) {
            String field = request.getUrl();
            String value;
            try {
                value = objectMapper.writeValueAsString(request);
            } catch (JsonProcessingException e) {
                logger.error("can not push request[{}]", field);
                return;
            }
            template.<String, String>opsForHash().put(getItemKey(task), field, value);
        }
    }
}
