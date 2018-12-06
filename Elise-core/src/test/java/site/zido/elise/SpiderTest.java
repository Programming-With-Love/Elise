package site.zido.elise;

import org.junit.Test;
import site.zido.elise.events.EventListener;
import site.zido.elise.events.SingleEventListener;
import site.zido.elise.select.configurable.ConfigurableUrlFinder;
import site.zido.elise.select.configurable.DefExtractor;
import site.zido.elise.select.configurable.DefRootExtractor;
import site.zido.elise.select.configurable.ExpressionType;

import java.util.concurrent.CountDownLatch;

/**
 * SpiderTest
 *
 * @author zido
 */
public class SpiderTest {
    /**
     * 简单单页爬取测试
     */
    @Test
    public void testOnePage() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        //构造爬虫
        //爬虫默认为异步运行
        Spider spider = SpiderBuilder.defaults();

        //构造抓取器
        DefRootExtractor extractor = new DefRootExtractor("article");
        extractor.setType(ExpressionType.CSS).setValue(".page-container>.blog");
        extractor.addTargetUrl(new ConfigurableUrlFinder("zido.site/?$"));
        extractor.addChildren(new DefExtractor("title")
                .setType(ExpressionType.CSS)
                .setValue("h2.blog-header-title"));
        extractor.addChildren(new DefExtractor("description")
                .setValue("p.blog-content")
                .setType(ExpressionType.CSS));

        //为抓取器部署抓取任务
        spider.of(extractor)
                //获取任务操作句柄后添加一个事件监听器
                .addEventListener(new SingleEventListener() {
                    /**
                     * 当任务完成之后，测试线程停止等待
                     */
                    @Override
                    public void onSuccess() {
                        latch.countDown();
                    }
                })
                //为该任务添加一个入口
                .execute("http://zido.site")
                //让爬虫抓取完该任务后取消运行，如果传入true会立即取消运行
                .cancel(false);

        latch.await();
    }

    @Test
    public void testMultiPage() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Spider spider = SpiderBuilder.defaults();
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
        spider.of(extractor).execute("http://github.com/zidoshare")
                .addEventListener(new SingleEventListener() {
                    @Override
                    public void onSuccess() {
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
    public void testCancel() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Spider spider = SpiderBuilder.defaults();
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
        spider.addEventListener(new EventListener() {
            @Override
            public void onCancel() {
                latch.countDown();
            }
        });
        spider.of(extractor).execute("http://github.com/zidoshare");
        Thread.sleep(3000);
        spider.cancel(true);
        latch.await();
    }
}
