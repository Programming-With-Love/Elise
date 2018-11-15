package site.zido.elise;

import java.util.List;

public interface EventListener {
    void onDownloadSuccess(Task task, Request request, Page page);

    void onDownloadError(Task task, Request request, Page page);

    void onSaveSuccess(Task task, List<ResultItem> resultItems);

    void onSaveError(Task task, List<ResultItem> resultItems);

    void onSuccess(Task task, int size);

    void onPause(Task task);

    void onCancel(Task task);
}
