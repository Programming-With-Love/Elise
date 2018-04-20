package com.hnqc.ironhand.message;

/**
 * MessageManager
 *
 * @author zido
 * @date 2018/04/20
 */
public interface MessageManager<K, V> {
    /**
     * message listener
     *
     * @param <V> type of message
     */
    interface MessageListener<V> {
        void onListen(V message);
    }

    /**
     * send message
     *
     * @param type    type
     * @param message message
     */
    void send(K type, V message);

    /**
     * listen message
     *
     * @param type     type
     * @param listener listener
     */
    void listen(K type, MessageListener<V> listener);

}