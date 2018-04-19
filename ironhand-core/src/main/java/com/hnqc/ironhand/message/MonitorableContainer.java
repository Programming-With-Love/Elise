package com.hnqc.ironhand.message;

/**
 * Monitorable Manager
 *
 * @author zido
 * @date 2018/04/20
 */
public interface MonitorableContainer {
    /**
     * get the container size by type
     *
     * @param type type
     * @return container size
     */
    int size(String type);

    /**
     * See how many messages are in the message container
     *
     * @return the size of message container
     */
    int blockSize();

    /**
     * Check if the message container is empty
     *
     * @return true/false
     */
    default boolean empty() {
        return blockSize() == 0;
    }
}
