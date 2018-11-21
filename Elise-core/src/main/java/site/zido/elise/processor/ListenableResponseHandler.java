package site.zido.elise.processor;

public interface ListenableResponseHandler extends ResponseHandler {
    void addEventListener(ProcessorEventListener listener);
}
