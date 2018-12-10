package site.zido.elise.events;

import site.zido.elise.http.Request;
import site.zido.elise.http.Response;
import site.zido.elise.processor.ResultItem;
import site.zido.elise.task.Task;
import site.zido.elise.utils.EventUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * The type Single listener container.
 *
 * @author zido
 */
public final class SingleListenerContainer implements TaskEventListener {
    private Set<SingleEventListener> listeners = new HashSet<>();
    private long taskId;
    private RecyclingCallback callback;

    /**
     * Instantiates a new Single listener container.
     *
     * @param taskId the task id
     */
    public SingleListenerContainer(long taskId) {
        this.taskId = taskId;
    }

    /**
     * Sets callback.
     *
     * @param callback the callback
     */
    public void setCallback(RecyclingCallback callback) {
        this.callback = callback;
    }

    /**
     * Add listener.
     *
     * @param listener the listener
     */
    public void addListener(SingleEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void onDownloadSuccess(Task task, Request request, Response response) {
        if (this.taskId == task.getId()) {
            EventUtils.notifyListeners(listeners, listener -> listener.onDownloadSuccess(request, response));
        }
    }

    @Override
    public void onDownloadError(Task task, Request request, Response response) {
        if (this.taskId == task.getId()) {
            EventUtils.notifyListeners(listeners, listener -> listener.onDownloadSuccess(request, response));
        }
    }

    @Override
    public void onSuccess(Task task) {
        if (this.taskId == task.getId()) {
            EventUtils.notifyListeners(listeners, SingleEventListener::onSuccess);
        }
        callback.onRecycling();
    }

    @Override
    public void onPause(Task task) {
        if (this.taskId == task.getId()) {
            EventUtils.notifyListeners(listeners, SingleEventListener::onPause);
        }
    }

    @Override
    public void onRecover(Task task) {
        if (this.taskId == task.getId()) {
            EventUtils.notifyListeners(listeners, SingleEventListener::onRecover);
        }
    }

    @Override
    public void onCancel(Task task) {
        if (this.taskId == task.getId()) {
            EventUtils.notifyListeners(listeners, SingleEventListener::onCancel);
        }
        callback.onRecycling();
    }

    @Override
    public void onSaveSuccess(Task task, ResultItem resultItem) {
        if (this.taskId == task.getId()) {
            EventUtils.notifyListeners(listeners, listener -> listener.onSaveSuccess(resultItem));
        }
    }

    /**
     * The interface Recycling callback.
     *
     * @author zido
     */
    @FunctionalInterface
    public interface RecyclingCallback {
        /**
         * On recycling.
         */
        void onRecycling();
    }
}
