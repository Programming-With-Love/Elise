package site.zido.elise.scheduler;

/**
 * Monitorable Manager
 *
 * @author zido
 */
public interface MonitorableScheduler {

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
