package site.zido.elise;

import site.zido.elise.http.impl.DefaultResponse;
import site.zido.elise.http.Request;
import site.zido.elise.processor.ProcessorEventListener;

public interface EventListener extends ProcessorEventListener, java.util.EventListener {
    default void onDownloadSuccess(Task task, Request request, DefaultResponse response) {
    }

    default void onDownloadError(Task task, Request request, DefaultResponse response) {
    }

    default void onSuccess(Task task) {
    }

    default void onPause(Task task) {
    }

    default void onRecover(Task task){
    }

    default void onCancel(Task task) {
    }

    default void onPause() {
    }

    default void onCancel() {
    }
}
