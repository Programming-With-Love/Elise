package site.zido.elise.client.scheduler;

import site.zido.elise.http.Request;
import site.zido.elise.http.Response;
import site.zido.elise.scheduler.AbstractScheduler;
import site.zido.elise.task.Task;

/**
 * 同步任务调度器简单实现
 *
 * @author zido
 */
public class SyncTaskScheduler extends AbstractScheduler {
    private boolean start = true;

    @Override
    protected void pushWhenNoDuplicate(Task task, Request request) {
        if (!start) {
            return;
        }
        Response response = super.onDownload(task, request);
        super.onProcess(task, request, response);
    }

    @Override
    public void cancel(boolean ifRunning) {
        start = false;
    }
}
