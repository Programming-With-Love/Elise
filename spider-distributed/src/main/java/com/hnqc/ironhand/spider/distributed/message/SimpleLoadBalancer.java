package com.hnqc.ironhand.spider.distributed.message;

import com.hnqc.ironhand.spider.utils.ValidateUtils;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Simple Load Balancer.
 *
 * @author zido
 * @date 2018/04/17
 */
public class SimpleLoadBalancer extends Vector<Object> implements LoadBalancer {
    private int current = 0;
    private Lock lock = new ReentrantLock();

    public SimpleLoadBalancer() {

    }

    public SimpleLoadBalancer(List objects) {
        super(objects);
        current = 0;
    }

    @Override
    public Object getNext() {
        lock.lock();
        try {
            if (ValidateUtils.isEmpty(this)) {
                return null;
            }
            int index = (current) % this.size();
            Object obj = this.get(index);
            current++;
            return obj;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void addAndRemove(Object element, Object front) {
        lock.lock();
        try {
            if (front != null) {
                remove(front);
            }
            if (element != null) {
                add(element);
            }
        } finally {
            lock.unlock();
        }
    }
}
