package site.zido.elise.processor;

/**
 * The interface Listenable response handler.
 *
 * @author zido
 */
public interface ListenableResponseHandler extends ResponseHandler {
    /**
     * Add event listener.
     *
     * @param listener the listener
     */
    void addEventListener(ProcessorEventListener listener);

    void removeEventListener(ProcessorEventListener listener);
}
