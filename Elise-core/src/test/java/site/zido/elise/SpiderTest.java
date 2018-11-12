package site.zido.elise;

import org.junit.Assert;
import org.junit.Test;
import site.zido.elise.processor.CrawlResult;
import site.zido.elise.select.Fragment;
import site.zido.elise.select.configurable.ConfigurableUrlFinder;
import site.zido.elise.select.configurable.DefExtractor;
import site.zido.elise.select.configurable.DefRootExtractor;
import site.zido.elise.select.configurable.ExpressionType;
import site.zido.elise.utils.IdWorker;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * SpiderTest
 *
 * @author zido
 */
public class SpiderTest {
    @Test
    public void testOnePage() throws InterruptedException, ExecutionException {
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
        CrawlResult result = spider.addUrl(task, "http://zido.site");
        Assert.assertEquals(8, result.count());

        extractResultItem(result);
    }

    private void extractResultItem(CrawlResult result) {
        int i = 1;
        for (ResultItem item : result) {
            Map<String, List<Fragment>> all = item.getAll();
            for (String fieldName : all.keySet()) {
                List<Fragment> fragments = all.get(fieldName);
                for (Fragment fragment : fragments) {
                    System.out.println(fieldName + "[" + (i) + "]:" + fragment.text());
                }
            }
            i++;
        }
    }

    @Test
    public void testMultiPage() throws ExecutionException, InterruptedException {
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
        CrawlResult result = spider.addUrl(task, "https://github.com/zidoshare");
        extractResultItem(result);
    }
}
