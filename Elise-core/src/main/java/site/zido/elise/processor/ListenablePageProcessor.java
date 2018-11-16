package site.zido.elise.processor;

public interface ListenablePageProcessor extends PageProcessor {
    void addEventListener(ProcessorEventListener listener);
}
