package site.zido.elise;

import org.junit.Test;
import site.zido.elise.select.configurable.ConfigurableUrlFinder;
import site.zido.elise.select.configurable.DefExtractor;
import site.zido.elise.select.configurable.DefRootExtractor;
import site.zido.elise.select.configurable.ExpressionType;
import site.zido.elise.utils.IdWorker;

import java.util.concurrent.CountDownLatch;

/**
 * SpiderTest
 *
 * @author zido
 */
public class SpiderTest {
    @Test
    public void testOnePage() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
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
        spider.addUrl(task, "http://zido.site");
        spider.addEventListener(new EventListener() {
            @Override
            public void onSuccess(Task task) {
                spider.cancel(true);
            }

            @Override
            public void onCancel() {
                latch.countDown();
            }
        });
        latch.await();
    }

    @Test
    public void testMultiPage() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Spider spider = Spider.defaults();
        DefRootExtractor extractor = new DefRootExtractor("project");
        extractor.addTargetUrl(new ConfigurableUrlFinder("github.com/zidoshare/[^/]*$"));
        extractor.addHelpUrl(new ConfigurableUrlFinder("github.com/zidoshare/[^/]*$"));
        extractor.addChildren(new DefExtractor("title")
                .setType(ExpressionType.XPATH)
                .setValue("//*[@id=\"js-repo-pjax-container\"]/div[1]/div/h1/strong/a"));
        extractor.addChildren(new DefExtractor("description")
                .setType(ExpressionType.XPATH)
                .setValue("//*[@id=\"repo-meta-edit\"]/summary/div[1]/div/span[1]/div/span"));
        extractor.addChildren(new DefExtractor("readme")
                .setType(ExpressionType.XPATH)
                .setValue("//*[@id=\"readme\"]/div[2]"));
        Task task = new DefaultTask(IdWorker.nextId(), new Site(), extractor);
        spider.addUrl(task,"http://github.com/zidoshare");
        spider.addEventListener(new EventListener() {
            @Override
            public void onSuccess(Task task) {
                spider.cancel(true);
            }

            @Override
            public void onCancel() {
                latch.countDown();
            }
        });
        latch.await();
    }
}
