package com.hnqc.ironhand.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hnqc.ironhand.Request;
import com.hnqc.ironhand.Task;
import com.hnqc.ironhand.scheduler.AbstractDistributedScheduler;
import com.hnqc.ironhand.utils.ValidateUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * Redis-based distributed task scheduling,
 *
 * @author zido
 * @date 2018/04/15
 */
public class RedisTemplateScheduler extends AbstractDistributedScheduler {
    private RedisTemplate<String, String> template;
    private final ObjectMapper objectMapper;

    public RedisTemplateScheduler(RedisTemplate<String, String> template) {
        this(template, new ObjectMapper());
        this.objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.template = template;
        setDuplicateRemover(this);
    }

    public RedisTemplateScheduler(RedisTemplate<String, String> template, ObjectMapper objectMapper) {
        Assert.notNull(objectMapper, "'objectMapper' must not be null.");
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean isDuplicate(Request request, Task task) {
        return template.opsForSet().add(getSetKey(task), request.getUrl()) == 0;
    }

    @Override
    public void resetDuplicateCheck(Task task) {
        template.delete(getSetKey(task));
    }

    @Override
    public int getLeftRequestsCount(Task task) {
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
