package site.zido.elise.events;

import site.zido.elise.processor.ProcessorEventListener;

/**
 * The interface Event listener.
 *
 * @author zido
 */
public interface EventListener extends ProcessorEventListener, java.util.EventListener {
    /**
     * On pause.
     */
    default void onPause() {
    }

    /**
     * On cancel.
     */
    default void onCancel() {
    }
}
