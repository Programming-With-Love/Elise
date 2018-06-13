package site.zido.elise;

import site.zido.elise.scheduler.TaskScheduler;
import site.zido.elise.scheduler.SimpleTaskScheduler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * TaskSchedulerTest
 *
 * @author zido
 */
public class TaskSchedulerTest {
    private Task task;

    @Before
    public void setUp() {
        this.task = new Site().toTask();
    }

    @Test
    public void testListen() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        TaskScheduler manager = new SimpleTaskScheduler();
        manager.registerAnalyzer((task, request, page) -> {
            Assert.assertEquals("www.baidu.com", request.getUrl());
            latch.countDown();
        });

        manager.registerDownloader((task, request) -> {
            Assert.assertEquals("www.1.com", request.getUrl());
            latch.countDown();
        });
        manager.registerDownloader((task, request) -> {
            Assert.assertEquals("www.1.com", request.getUrl());
            latch.countDown();
        });

        manager.process(task, new Request("www.baidu.com"), new Page());
        manager.pushRequest(task, new Request("www.1.com"));
        manager.pushRequest(task, new Request("www.1.com"));
        latch.await(3, TimeUnit.SECONDS);
        Assert.assertEquals(0, latch.getCount());
    }
}
