package site.zido.elise;

import org.junit.Assert;
import org.junit.Test;
import site.zido.elise.configurable.ConfigurableUrlFinder;
import site.zido.elise.configurable.DefExtractor;
import site.zido.elise.configurable.DefRootExtractor;
import site.zido.elise.configurable.ExpressionType;
import site.zido.elise.utils.IdWorker;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * SpiderTest
 *
 * @author zido
 */
public class SpiderTest {
    @Test
    public void testRun() throws InterruptedException, ExecutionException {
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
        Future<CrawlResult> future = spider.addUrl(task, "http://zido.site");
        CrawlResult result = future.get();
        Assert.assertEquals(8, result.size());
        for (ResultItem item : result) {
            System.out.println(item.getAll());
        }
    }

    @Test
    public void testPause() throws ExecutionException, InterruptedException {
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
        CrawlResult result = spider.addUrl(task, "http://github.com/zidoshare").get();
        for (ResultItem resultItem : result) {
            System.out.println(resultItem.getAll());
        }
    }
}
