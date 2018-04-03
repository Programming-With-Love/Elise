package com.hnqc.ironhand.spider.distributed.scheduler;

import com.alibaba.fastjson.JSON;
import com.hnqc.ironhand.spider.Request;
import com.hnqc.ironhand.spider.Task;
import com.hnqc.ironhand.spider.scheduler.DuplicateRemovedScheduler;
import com.hnqc.ironhand.spider.scheduler.DuplicateRemover;
import com.hnqc.ironhand.spider.scheduler.MonitorableScheduler;
import com.hnqc.ironhand.spider.utils.ValidateUtils;
import org.apache.commons.codec.digest.DigestUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 基于redis的分布式任务调度实现
 * 
 */
public class RedisScheduler extends DuplicateRemovedScheduler implements MonitorableScheduler, DuplicateRemover {

    protected JedisPool pool;

    private static final String QUEUE_PREFIX = "queue_";

    private static final String SET_PREFIX = "set_";

    private static final String ITEM_PREFIX = "item_";

    public RedisScheduler(String host) {
        this(new JedisPool(new JedisPoolConfig(), host));
    }

    public RedisScheduler(JedisPool pool) {
        this.pool = pool;
        setDuplicateRemover(this);
    }

    @Override
    public void resetDuplicateCheck(Task task) {
        try (Jedis jedis = pool.getResource()) {
            jedis.del(getSetKey(task));
        }
    }

    @Override
    public boolean isDuplicate(Request request, Task task) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.sadd(getSetKey(task), request.getUrl()) == 0;
        }

    }

    @Override
    protected void pushWhenNoDuplicate(Request request, Task task) {
        try (Jedis jedis = pool.getResource()) {
            jedis.rpush(getQueueKey(task), request.getUrl());
            if (checkForAdditionalInfo(request)) {
                String field = DigestUtils.shaHex(request.getUrl());
                String value = JSON.toJSONString(request);
                jedis.hset((ITEM_PREFIX + task.getID()), field, value);
            }
        }
    }

    private boolean checkForAdditionalInfo(Request request) {
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

    @Override
    public synchronized Request poll(Task task) {
        try (Jedis jedis = pool.getResource()) {
            String url = jedis.lpop(getQueueKey(task));
            if (url == null) {
                return null;
            }
            String key = ITEM_PREFIX + task.getID();
            String field = DigestUtils.shaHex(url);
            byte[] bytes = jedis.hget(key.getBytes(), field.getBytes());
            if (bytes != null) {
                return JSON.parseObject(new String(bytes), Request.class);
            }
            return new Request(url);
        }
    }

    protected String getSetKey(Task task) {
        return SET_PREFIX + task.getID();
    }

    protected String getQueueKey(Task task) {
        return QUEUE_PREFIX + task.getID();
    }

    protected String getItemKey(Task task) {
        return ITEM_PREFIX + task.getID();
    }

    @Override
    public int getLeftRequestsCount(Task task) {
        try (Jedis jedis = pool.getResource()) {
            Long size = jedis.llen(getQueueKey(task));
            return size.intValue();
        }
    }

    @Override
    public int getTotalRequestsCount(Task task) {
        try (Jedis jedis = pool.getResource()) {
            Long size = jedis.scard(getSetKey(task));
            return size.intValue();
        }
    }
}
