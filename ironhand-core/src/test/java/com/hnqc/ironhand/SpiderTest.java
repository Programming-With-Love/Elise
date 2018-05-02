package com.hnqc.ironhand;

import com.hnqc.ironhand.configurable.*;
import com.hnqc.ironhand.downloader.HttpClientDownloader;
import com.hnqc.ironhand.extractor.ModelExtractor;
import com.hnqc.ironhand.pipeline.AbstractSqlPipeline;
import com.hnqc.ironhand.scheduler.SimpleTaskScheduler;
import com.hnqc.ironhand.pipeline.ConsolePipeline;
import com.hnqc.ironhand.processor.ExtractorPageProcessor;
import com.hnqc.ironhand.selector.Selectable;
import com.hnqc.ironhand.utils.IdWorker;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * SpiderTest
 *
 * @author zido
 * @date 2018/04/19
 */
public class SpiderTest {
    @Test
    public void testRun() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(6);
        Spider spider = new Spider(new SimpleTaskScheduler(),
                new ExtractorPageProcessor(),
                new HttpClientDownloader(),
                new ConsolePipeline() {
                    @Override
                    public void process(ResultItem resultItem, Task task) {
                        latch.countDown();
                        super.process(resultItem, task);
                    }
                }).start().addUrl(new ExtractorTask() {
            @Override
            public ModelExtractor modelExtractor() {
                return new ModelExtractor() {
                    @Override
                    public ResultItem extract(Page page) {
                        String author = page.getUrl().regex("https://github\\.com/(\\w+)/.*").toString();
                        Selectable name = page.html().xpath("//h1[@class='public']/strong/a/text()");
                        return new ResultItem()
                                .put("author", author)
                                .put("name", name)
                                .setSkip(name == null || author == null);
                    }

                    @Override
                    public List<String> extractLinks(Page page) {
                        return page.html().links().regex("(https://github\\.com/zidoshare/[\\w\\-]+)").all();
                    }
                };
            }

            @Override
            public Long getId() {
                return IdWorker.nextId();
            }

            @Override
            public Site getSite() {
                return new Site().setRetryTimes(3).setSleepTime(1).setCycleRetryTimes(3);
            }
        }, "https://github.com/zidoshare");
        Assert.assertTrue(latch.await(60, TimeUnit.SECONDS));
    }

    @Test
    public void testMulti() throws InterruptedException {
        DefRootExtractor def = new DefRootExtractor();
        def.setName("hnqc_core_member.member_spider");
        def.addTargetUrl(new ConfigurableUrlFinder()
                .setType(ConfigurableUrlFinder.Type.REGEX)
                .setValue("http://ldzl\\.people\\.com\\.cn/dfzlk/front/personPage[0-9]+\\.htm$"));
        def.addHelpUrl(new ConfigurableUrlFinder()
                .setType(ConfigurableUrlFinder.Type.REGEX)
                .setValue("http://ldzl\\.people\\.com\\.cn/dfzlk/front/personPage[0-9]+\\.htm$"));
        def.addHelpUrl(new ConfigurableUrlFinder()
                .setType(ConfigurableUrlFinder.Type.REGEX)
                .setValue("http://ldzl\\.people\\.com\\.cn/dfzlk/front/.*+\\.htm"));
        def.addChildren(new DefExtractor()
                .setName("member_name")
                .setType(ExpressionType.XPATH)
                .setValue("//div[@class='fl p2j_text_center title_2j']/h2[1]/text()"));
        def.addChildren(new DefExtractor()
                .setName("text")
                .setValue("//div[@class='fl p2j_text_center title_2j']/div[@class='p2j_text']/tidyText()")
                .setType(ExpressionType.XPATH));
        def.addChildren(new DefExtractor()
                .setName("origin")
                .setType(ExpressionType.XPATH)
                .setValue("//title/text()"));
        def.addChildren(new DefExtractor()
                .setName("url")
                .setType(ExpressionType.REGEX)
                .setValue("(.*\\.com.cn).*")
                .setSource(Extractor.Source.URL)
                .setNullable(true));

        CountDownLatch latch = new CountDownLatch(20);
        new Spider(new SimpleTaskScheduler(),
                new ExtractorPageProcessor(),
                new HttpClientDownloader(),
                new AbstractSqlPipeline() {
                    @Override
                    public void onInsert(String sql, Object[] object) {
                        System.out.println("sql = [" + sql + "], object = [" + Arrays.toString(object) + "]");
                        latch.countDown();
                    }
                }).start().addUrl(new DistributedTask(IdWorker.nextId(), new Site(), def), "http://ldzl.people.com.cn/dfzlk/front/firstPage.htm");
        latch.await(60, TimeUnit.SECONDS);
    }
}
