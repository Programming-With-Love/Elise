package site.zido.elise.message;

import site.zido.elise.*;
import site.zido.elise.scheduler.SimpleTaskScheduler;
import site.zido.elise.selector.PlainText;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * ThreadTaskSchedulerTest
 *
 * @author zido
 */
public class ThreadTaskSchedulerTest {
    @Test
    public void testListen() throws InterruptedException {
        DefaultExtractorTask defaultExtractorTask = new DefaultExtractorTask(1L, new Site(), null);
        CountDownLatch latch = new CountDownLatch(2);
        SimpleTaskScheduler scheduler = new SimpleTaskScheduler();
        scheduler.registerAnalyzer((task, request, page) -> {
            Long id = task.getId();
            Assert.assertEquals(1, id.longValue());
            Assert.assertEquals("http://www.baidu.com", request.getUrl());
            Assert.assertEquals("i'm test", page.getUrl().toString());
            latch.countDown();
        });

        scheduler.registerDownloader(((task, request) -> {
            Long id = task.getId();
            Assert.assertEquals("http://www.baidu.com", request.getUrl());
            Assert.assertEquals(1, id.longValue());
            latch.countDown();
        }));

        Request request = new Request();
        request.setUrl("http://www.baidu.com");
        scheduler.pushRequest(defaultExtractorTask, request);
        //此请求将被过滤
        scheduler.pushRequest(defaultExtractorTask, request);
        Page page = new Page();
        page.setUrl("i'm test");
        scheduler.process(defaultExtractorTask, request, page);
        Assert.assertTrue(latch.await(5, TimeUnit.SECONDS));
    }
}
