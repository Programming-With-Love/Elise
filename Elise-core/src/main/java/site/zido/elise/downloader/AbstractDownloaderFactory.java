package site.zido.elise.downloader;

import site.zido.elise.task.Task;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractDownloaderFactory implements DownloaderFactory {
    protected Map<Task, Downloader> downloaderContainer = new ConcurrentHashMap<>();
    @Override
    public void release(Task task) {
        //release downloader
        downloaderContainer.remove(task);
    }
}
