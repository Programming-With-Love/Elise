package site.zido.elise.processor;

import site.zido.elise.ResultItem;
import site.zido.elise.Task;

import java.util.EventListener;

public interface ProcessorEventListener extends EventListener {
    default void onSaveSuccess(Task task, ResultItem resultItems) {
    }

    default void onSaveError(Task task, ResultItem resultItems) {
    }
}
