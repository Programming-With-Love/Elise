package site.zido.elise;

import org.junit.Assert;
import org.junit.Test;
import site.zido.elise.configurable.ConfigurableUrlFinder;
import site.zido.elise.configurable.DefExtractor;
import site.zido.elise.configurable.DefRootExtractor;
import site.zido.elise.configurable.ExpressionType;
import site.zido.elise.downloader.HttpClientDownloader;
import site.zido.elise.extractor.ModelExtractor;
import site.zido.elise.pipeline.ConsolePipeline;
import site.zido.elise.processor.ExtractorPageProcessor;
import site.zido.elise.scheduler.SimpleTaskScheduler;
import site.zido.elise.selector.Selectable;
import site.zido.elise.utils.IdWorker;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * SpiderTest
 *
 * @author zido
 */
public class SpiderTest {
    @Test
    public void testMulti() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(6);
        Spider spider = Spider.builder(new SimpleTaskScheduler(1))
                .setDownloader(new HttpClientDownloader())
                .setPageProcessor(new ExtractorPageProcessor())
                .addPipeline(new ConsolePipeline(){
                    @Override
                    public void process(ResultItem resultItem, Task task) {
                        super.process(resultItem, task);
                        latch.countDown();
                    }
                }).build().start();
        DefRootExtractor extractor = new DefRootExtractor("article");
        extractor.setType(ExpressionType.CSS).setValue(".page-container>.blog");
        extractor.addTargetUrl(new ConfigurableUrlFinder("zido.site/?$"));
        extractor.addChildren(new DefExtractor("title")
                .setType(ExpressionType.CSS)
                .setValue("h2.blog-header-title"));
        extractor.addChildren(new DefExtractor("description")
                .setValue("p.blog-content")
                .setType(ExpressionType.CSS));
        DefaultExtractorTask task = new DefaultExtractorTask(IdWorker.nextId(), new Site(), extractor);
        spider.addUrl(task,"http://zido.site");
        latch.await();
    }
}
