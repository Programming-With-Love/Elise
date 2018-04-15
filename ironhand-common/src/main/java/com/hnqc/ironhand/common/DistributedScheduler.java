package com.hnqc.ironhand.common;

import com.alibaba.fastjson.JSON;
import com.hnqc.ironhand.spider.Request;
import com.hnqc.ironhand.spider.Task;
import com.hnqc.ironhand.spider.distributed.scheduler.AbstractDistributedScheduler;
import com.hnqc.ironhand.spider.utils.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 分布式任务调度实现类，基于spring redis template
 *
 * @author zido
 * @date 2018/04/15
 */
@Component
public class DistributedScheduler extends AbstractDistributedScheduler {
    private RedisTemplate<String, String> template;

    @Autowired
    public DistributedScheduler(RedisTemplate<String, String> template) {
        super();
        this.template = template;
        setDuplicateRemover(this);
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
            return JSON.parseObject(value, Request.class);
        }
        return new Request(url);
    }

    @Override
    protected void pushWhenNoDuplicate(Request request, Task task) {
        template.opsForList().rightPush(getQueueKey(task), request.getUrl());
        if (checkForAdditionalInfo(request)) {
            String field = request.getUrl();
            String value = JSON.toJSONString(request);
            template.<String, String>opsForHash().put(getItemKey(task), field, value);
        }
    }
}
