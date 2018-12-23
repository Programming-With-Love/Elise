package site.zido.elise.downloader;

import site.zido.elise.task.Task;

/**
 * The interface Downloader factory.
 *
 * @author zido
 */
public interface DownloaderFactory {
    /**
     * Create downloader.
     *
     * @param task the task
     * @return the downloader
     */
    Downloader create(Task task);

    void release(Task task);
}
