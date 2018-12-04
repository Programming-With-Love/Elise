package site.zido.elise;

import site.zido.elise.http.impl.DefaultRequest;
import site.zido.elise.scheduler.TaskScheduler;
import site.zido.elise.select.configurable.DefRootExtractor;
import site.zido.elise.utils.Asserts;
import site.zido.elise.utils.IdWorker;

/**
 * the main spider
 *
 * @author zido
 */
public abstract class AbstractSpider implements Spider, TaskScheduler {
    @Override
    public Spider crawl(DefRootExtractor extractor, String... url) {
        Asserts.notEmpty(url);
        final DefaultTask task = new DefaultTask(IdWorker.nextId(), extractor);
        for (String s : url) {
            Asserts.notEmpty(url);
            pushRequest(task, new DefaultRequest(s));
        }
        return this;
    }
}
