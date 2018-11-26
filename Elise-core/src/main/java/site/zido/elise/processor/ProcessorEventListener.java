package site.zido.elise.processor;

import site.zido.elise.ResultItem;
import site.zido.elise.Task;

import java.util.EventListener;

/**
 * The interface Processor event listener.
 *
 * @author zido
 */
public interface ProcessorEventListener extends EventListener {
    /**
     * On save success.
     *
     * @param task        the task
     * @param resultItems the result items
     */
    default void onSaveSuccess(Task task, ResultItem resultItems) {
    }

    /**
     * On save error.
     *
     * @param task        the task
     * @param resultItems the result items
     */
    default void onSaveError(Task task, ResultItem resultItems) {
    }
}
