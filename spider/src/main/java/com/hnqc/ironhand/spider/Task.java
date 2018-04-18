package com.hnqc.ironhand.spider;

/**
 * Task interface
 *
 * @author zido
 * @date 2018/04/18
 */
public interface Task {
    /**
     * Get task id
     *
     * @return id
     */
    Long getId();

    /**
     * Get website configuration
     *
     * @return site
     */
    Site getSite();
}
