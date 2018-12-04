package site.zido.elise.events;

/**
 * The interface Event support.
 *
 * @author zido
 */
public interface EventSupport {
    /**
     * Add event listener.
     *
     * @param listener the listener
     */
    void addEventListener(EventListener listener);

    /**
     * Remove event listener.
     *
     * @param listener the listener
     */
    void removeEventListener(EventListener listener);
}
