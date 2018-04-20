package com.hnqc.ironhand.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Abstract Communication Manager
 *
 * @author zido
 * @date 2018/04/19
 */
public class ExecutorContainer {

    private static Logger logger = LoggerFactory.getLogger(ExecutorContainer.class);

    private AtomicInteger stat = new AtomicInteger(STAT_INIT);

    private final static int STAT_INIT = 0;

    private final static int STAT_RUNNING = 1;

    private final static int STAT_STOPPED = 2;

    private Map<String, LoadBalancer> balancerContainer = new ConcurrentHashMap<>();

    private LoadBalancer loadBalancer;

    public ExecutorContainer() {
        this(new SimpleLoadBalancer());
    }

    public ExecutorContainer(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    public void register(String type, Object target) {
        LoadBalancer loadBalancer = balancerContainer.get(type);
        if (loadBalancer == null) {
            loadBalancer = this.loadBalancer.newClone();
            balancerContainer.put(type, loadBalancer);
        }
        loadBalancer.add(target);
    }

    public void remove(String type, Object target) {
        LoadBalancer loadBalancer = balancerContainer.get(type);
        if (loadBalancer != null) {
            loadBalancer.remove(target);
        } else {
            loadBalancer = this.loadBalancer.newClone();
            balancerContainer.put(type, loadBalancer);
        }
    }

//    @Override
//    public void listen() {
//        checkRunningStat();
//    }
//
//    @Override
//    public void stop() {
//        if (stat.compareAndSet(STAT_RUNNING, STAT_STOPPED)) {
//            logger.info("container manager stop success!");
//        } else {
//            logger.info("container stop fail!");
//        }
//    }

    protected Object getTargetByType(String type) {
        LoadBalancer loadBalancer = balancerContainer.get(type);
        if (loadBalancer == null) {
            loadBalancer = this.loadBalancer.newClone();
            balancerContainer.put(type, loadBalancer);
        }
        return loadBalancer.getNext();
    }

//    protected void checkRunningStat() {
//        while (true) {
//            int statNow = stat.get();
//            if (statNow == STAT_RUNNING) {
//                throw new IllegalStateException("message is already listening!");
//            }
//            if (stat.compareAndSet(statNow, STAT_RUNNING)) {
//                break;
//            }
//        }
//    }
//
//    @Override
//    public TaskScheduler.Status getStatus() {
//        return TaskScheduler.Status.fromValue(stat.get());
//    }
//
//    public boolean running() {
//        return STAT_RUNNING == stat.get();
//    }

    public int clientSize(String type) {
        LoadBalancer loadBalancer = balancerContainer.get(type);
        if (loadBalancer == null) {
            loadBalancer = this.loadBalancer.newClone();
            balancerContainer.put(type, loadBalancer);
        }
        return loadBalancer.size();
    }
}
