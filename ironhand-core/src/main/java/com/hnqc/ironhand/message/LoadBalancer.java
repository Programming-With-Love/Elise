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
     * add and remove
     *
     * @param element The element you want to add to the container
     * @param front   The element in the container you wish to delete
     */
    void removeAndAdd(Object element, Object front);

    /**
     * return new empty load balancer
     *
     * @return load balancer
     */
    LoadBalancer newClone();

    /**
     * return size of list
     * @return size
     */
    int size();
}
