package site.zido.elise;

import org.junit.Assert;
import org.junit.Test;
import site.zido.elise.events.EventListener;
import site.zido.elise.events.SingleEventListener;
import site.zido.elise.http.Http;
import site.zido.elise.http.RequestBody;
import site.zido.elise.http.RequestBuilder;
import site.zido.elise.http.impl.DefaultRequest;
import site.zido.elise.scheduler.NoDepuplicationProcessor;
import site.zido.elise.select.CssSelector;
import site.zido.elise.select.LinkSelector;
import site.zido.elise.select.configurable.ExtractorBuilder;
import site.zido.elise.select.configurable.FieldExtractorBuilder;
import site.zido.elise.select.configurable.ModelExtractor;

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
        //构造爬虫
        //爬虫默认为异步运行
        Spider spider = SpiderBuilder.create().setDuplicationProcessor(new NoDepuplicationProcessor()).build();

        //构造抓取器
        final ModelExtractor extractor = ExtractorBuilder.create("article")
                .source(new CssSelector(".page-container>.blog"))
                .addTargetUrl(new LinkSelector("zido.site/?$"))
                .addField(FieldExtractorBuilder.create("title")
                        .css("h2.blog-header-title").build())
                .addField(FieldExtractorBuilder.create("description")
                        .css("p.blog-content").build())
                .build();
        //为抓取器部署抓取任务
        spider.of(extractor)
                //获取任务操作句柄后添加一个事件监听器
                .addEventListener(new SingleEventListener() {
                    /**
                     * 当任务完成之后，测试线程停止等待
                     */
                    @Override
                    public void onSuccess(String name) {
                        Assert.assertEquals("article", name);
                    }
                })
                //为该任务添加一个入口
                .execute("http://zido.site")
                //让爬虫抓取完该任务后取消运行，如果传入true会立即取消运行
                .block()
                //为该任务添加一个入口
                .execute("http://zido.site")
                //让爬虫抓取完该任务后取消运行，如果传入true会立即取消运行
                .block();
    }

    @Test
    public void testMultiPage() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Spider spider = SpiderBuilder.defaults();
        ModelExtractor extractor = ExtractorBuilder.create("project")
                .addTargetUrl(new LinkSelector("github.com/zidoshare/[^/]*$"))
                .addHelpUrl(new LinkSelector("github.com/zidoshare/[^/]*$"))
                .addField(FieldExtractorBuilder.create("title")
                        .xpath("//*[@id=\"js-repo-pjax-container\"]/div[1]/div/h1/strong/a")
                        .build())
                .addField(FieldExtractorBuilder.create("description")
                        .xpath("//*[@id=\"repo-meta-edit\"]/summary/div[1]/div/span[1]/div/span")
                        .build())
                .addField(FieldExtractorBuilder.create("readme")
                        .xpath("//*[@id=\"readme\"]/div[2]")
                        .build())
                .build();
        spider.of(extractor).execute("http://github.com/zidoshare")
                .addEventListener(new SingleEventListener() {
                    @Override
                    public void onCancel() {
                        latch.countDown();
                    }
                }).cancel(true);
        latch.await();
    }

    @Test
    public void testCancel() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Spider spider = SpiderBuilder.defaults();
        ModelExtractor extractor = ExtractorBuilder.create("project")
                .addTargetUrl(new LinkSelector("github.com/zidoshare/[^/]*$"))
                .addHelpUrl(new LinkSelector("github.com/zidoshare/[^/]*$"))
                .addField(FieldExtractorBuilder.create("title")
                        .xpath("//*[@id=\"js-repo-pjax-container\"]/div[1]/div/h1/strong/a")
                        .build())
                .addField(FieldExtractorBuilder.create("description")
                        .xpath("//*[@id=\"repo-meta-edit\"]/summary/div[1]/div/span[1]/div/span")
                        .build())
                .addField(FieldExtractorBuilder.create("readme")
                        .xpath("//*[@id=\"readme\"]/div[2]")
                        .build())
                .build();
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

    @Test
    public void testSpider() throws InterruptedException {
        final Spider spider = SpiderBuilder.defaults();
        spider.of(ExtractorBuilder.create("login").build()).execute(RequestBuilder
                .post("http://my.scu.edu.cn/userPasswordValidate.portal")
                .bodyForm("Login.Token1=xxx&Login.Token2=xxx.&goto=http://my.scu.edu.cn/loginSuccess.portal&gotoOnFail=http://my.scu.edu.cn/loginFailure.portal")
                .build()).block();
    }
}
