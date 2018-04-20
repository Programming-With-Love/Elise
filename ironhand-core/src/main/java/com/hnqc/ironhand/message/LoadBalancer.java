package com.hnqc.ironhand.message;

/**
 * load balancer.Responsible for task scheduling load balancing.
 * <p>
 * Generally used for thread level, process level and distributed level.
 *
 * @author zido
 * @date 2018/04/17
 */
public interface LoadBalancer {
    /**
     * Take out the next available task
     *
     * @return next task
     */
    Object getNext();

    /**
     * add task
     *
     * @param object task
     * @return true/false
     */
    boolean add(Object object);

    /**
     * remove task
     *
     * @param object task
     * @return true/false
     */
    boolean remove(Object object);

    /**
     * return new empty load balancer
     *
     * @return load balancer
     */
    LoadBalancer newClone();

    /**
     * return clientSize of list
     * @return clientSize
     */
    int size();
}
