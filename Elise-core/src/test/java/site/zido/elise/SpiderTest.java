package site.zido.elise;

import org.junit.Test;
import site.zido.elise.configurable.ConfigurableUrlFinder;
import site.zido.elise.configurable.DefExtractor;
import site.zido.elise.configurable.DefRootExtractor;
import site.zido.elise.configurable.ExpressionType;
import site.zido.elise.utils.IdWorker;

import java.util.concurrent.CountDownLatch;

/**
 * SpiderTest
 *
 * @author zido
 */
public class SpiderTest {
    @Test
    public void testMulti() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(6);
        Spider spider = Spider.defaults();
        DefRootExtractor extractor = new DefRootExtractor("article");
        extractor.setType(ExpressionType.CSS).setValue(".page-container>.blog");
        extractor.addTargetUrl(new ConfigurableUrlFinder("zido.site/?$"));
        extractor.addChildren(new DefExtractor("title")
                .setType(ExpressionType.CSS)
                .setValue("h2.blog-header-title"));
        extractor.addChildren(new DefExtractor("description")
                .setValue("p.blog-content")
                .setType(ExpressionType.CSS));
        DefaultTask task = new DefaultTask(IdWorker.nextId(), new Site(), extractor);
        spider.addUrl(task,"http://zido.site");
        latch.await();
    }

    @Test
    public void testSimpleRun() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        latch.await();
    }
}
