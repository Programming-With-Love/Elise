package site.zido.elise.scheduler;

/**
 * load balancer.Responsible for task scheduling load balancing.
 * <p>
 * Generally used for thread level, process level and distributed level.
 *
 * @author zido
 */
public interface LoadBalancer<T> {
    /**
     * Take out the next available task
     *
     * @return next task
     */
    T getNext();

    /**
     * add task
     *
     * @param object task
     * @return true/false
     */
    boolean add(T object);

    /**
     * remove task
     *
     * @param object task
     * @return true/false
     */
    boolean remove(T object);

    <V> LoadBalancer<V> newClone();

    /**
     * return downloaderSize of list
     *
     * @return downloaderSize
     */
    int size();
}
