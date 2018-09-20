package site.zido.elise.scheduler;

import site.zido.elise.utils.ValidateUtils;
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
 */
public class SimpleLoadBalancer<T> implements LoadBalancer<T> {
    private static Logger logger = LoggerFactory.getLogger(SimpleLoadBalancer.class);
    private int current;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private List<T> list = new Vector<>();

    public SimpleLoadBalancer() {
        current = 0;
    }

    public SimpleLoadBalancer(List<T> objects) {
        list.addAll(objects);
        current = 0;
    }

    /**
     * if there is nothing, it will wait until an available object is returned
     *
     * @return object
     */
    @Override
    public T getNext() {
        lock.lock();
        try {
            if (ValidateUtils.isEmpty(list)) {
                return null;
            }
            int index = current;
            T obj = list.get(index);
            current = (current + 1) % list.size();
            return obj;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean add(T object) {
        if (!list.isEmpty()) {
            return list.add(object);
        } else {
            lock.lock();
            boolean add = list.add(object);
            if (add) {
                condition.signalAll();
            }
            lock.unlock();
            return add;
        }
    }

    @Override
    public boolean remove(T object) {
        lock.lock();
        try {
            return list.remove(object);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public synchronized <V> LoadBalancer<V> newClone() {
        return new SimpleLoadBalancer<V>();
    }

    @Override
    public int size() {
        return list.size();
    }
}
