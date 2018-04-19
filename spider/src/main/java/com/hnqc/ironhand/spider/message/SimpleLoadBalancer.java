package com.hnqc.ironhand.spider.message;

import com.hnqc.ironhand.spider.utils.ValidateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Simple Load Balancer.
 *
 * @author zido
 * @date 2018/04/17
 */
public class SimpleLoadBalancer implements LoadBalancer {
    private static Logger logger = LoggerFactory.getLogger(SimpleLoadBalancer.class);
    private int current = 0;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private int lockTime = 2;
    private List<Object> list = new Vector<>();

    public SimpleLoadBalancer() {

    }

    public SimpleLoadBalancer(List objects) {
        list.addAll(objects);
        current = 0;
    }

    /**
     * This method will never return null, if there is nothing, it will wait until an available object is returned
     *
     * @return object
     */
    @Override
    public Object getNext() {
        lock.lock();
        try {
            if (ValidateUtils.isEmpty(list)) {
                logger.warn("Balancer has not been available,waiting {} seconds", lockTime);
                try {
                    boolean await = condition.await(lockTime, TimeUnit.SECONDS);
                    if (!await) {
                        return null;
                    }
                } catch (InterruptedException e) {
                    logger.warn("Balancer has not been available for {} seconds ", lockTime);
                    return null;
                }
            }
            int index = current;
            Object obj = list.get(index);
            current = (current + 1) % list.size();
            return obj;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean add(Object object) {
        lock.lock();
        boolean add = list.add(object);
        if (add) {
            condition.signalAll();
        }
        lock.unlock();
        return add;
    }

    @Override
    public boolean remove(Object object) {
        return list.remove(object);
    }

    @Override
    public void removeAndAdd(Object element, Object front) {
        lock.lock();
        if (element != null) {
            remove(element);
        }
        if (front != null) {
            add(front);
        }
        lock.unlock();
    }

    @Override
    public synchronized LoadBalancer newClone() {
        return new SimpleLoadBalancer();
    }

    public SimpleLoadBalancer setLockTime(int lockTime) {
        this.lockTime = lockTime;
        return this;
    }
}
