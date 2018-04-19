package com.hnqc.ironhand.spider;

import com.hnqc.ironhand.spider.downloader.HttpClientDownloader;
import com.hnqc.ironhand.spider.extractor.ModelExtractor;
import com.hnqc.ironhand.spider.message.SimpleCommunicationManager;
import com.hnqc.ironhand.spider.message.ThreadCommunicationManager;
import com.hnqc.ironhand.spider.pipeline.ConsolePipeline;
import com.hnqc.ironhand.spider.processor.ExtractorPageProcessor;
import com.hnqc.ironhand.spider.scheduler.QueueScheduler;
import com.hnqc.ironhand.spider.selector.Selectable;
import com.hnqc.ironhand.utils.IdWorker;
import org.junit.Assert;
import org.junit.Test;

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
        Spider spider = new Spider(new ThreadCommunicationManager(),
                new ExtractorPageProcessor(),
                new HttpClientDownloader(),
                new QueueScheduler(),
                new ConsolePipeline() {
                    @Override
                    public void process(ResultItem resultItem, Task task) {
                        latch.countDown();
                        super.process(resultItem, task);
                    }
                }).start().addUrl(new ExtractorTask() {
            @Override
            public ModelExtractor getModelExtractor() {
                return new ModelExtractor() {
                    @Override
                    public ResultItem extract(Page page) {
                        String author = page.getUrl().regex("https://github\\.com/(\\w+)/.*").toString();
                        Selectable name = page.getHtml().xpath("//h1[@class='public']/strong/a/text()");
                        return new ResultItem()
                                .put("author", author)
                                .put("name", name)
                                .setSkip(name == null || author == null);
                    }

                    @Override
                    public List<String> extractLinks(Page page) {
                        return page.getHtml().links().regex("(https://github\\.com/zidoshare/[\\w\\-]+)").all();
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
}
