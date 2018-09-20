package site.zido.elise.scheduler;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import site.zido.elise.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimpleTaskSchedulerTest {
    private Task task;

    @Before
    public void setUp() {
        this.task = new Site().toTask();
    }

    @Test
    public void testWithDuplication() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        TaskScheduler manager = new SimpleTaskScheduler();
        Set<Integer> receiver = new HashSet<>();
        manager.registerAnalyzer((task, request, page) -> {
            Assert.assertEquals("www.baidu.com", request.getUrl());
            receiver.add(1);
            latch.countDown();
        });

        manager.registerDownloader((task, request) -> {
            Assert.assertEquals("UTF-8", request.getCharset());
            receiver.add(2);
            latch.countDown();
        });
        manager.registerDownloader((task, request) -> {
            Assert.assertEquals("UTF-8", request.getCharset());
            receiver.add(3);
            latch.countDown();
        });

        manager.process(task, new Request("www.baidu.com"), new Page());
        Request request = new Request("www.1.com");
        request.setCharset("UTF-8");
        manager.pushRequest(task, request);
        //will not received this message
        manager.pushRequest(task, request);
        Request request2 = new Request("www.2.com");
        request2.setCharset("UTF-8");
        manager.pushRequest(task, request2);
        boolean await = latch.await(3, TimeUnit.SECONDS);
        Assert.assertTrue("there are not enough message received", await);
        Assert.assertEquals("there are not enough receivers",3,receiver.size());
    }
    @Test
    public void testWithoutDuplication() throws InterruptedException {
        SimpleTaskScheduler manager = new SimpleTaskScheduler(1,new DuplicationProcessor() {
            @Override
            public boolean isDuplicate(Request request, Task task) {
                return false;
            }

            @Override
            public void resetDuplicateCheck(Task task) {

            }

            @Override
            public int getTotalRequestsCount(Task task) {
                return 0;
            }
        });
        CountDownLatch latch = new CountDownLatch(4);
        Set<Integer> receiver = new HashSet<>();
        manager.registerAnalyzer((task, request, page) -> {
            Assert.assertEquals("www.baidu.com", request.getUrl());
            receiver.add(1);
            latch.countDown();
        });

        manager.registerDownloader((task, request) -> {
            Assert.assertEquals("UTF-8", request.getCharset());
            receiver.add(2);
            latch.countDown();
        });
        manager.registerDownloader((task, request) -> {
            Assert.assertEquals("UTF-8", request.getCharset());
            receiver.add(3);
            latch.countDown();
        });

        manager.process(task, new Request("www.baidu.com"), new Page());
        Request request = new Request("www.1.com");
        request.setCharset("UTF-8");
        manager.pushRequest(task, request);
        //will received this message
        manager.pushRequest(task, request);
        Request request2 = new Request("www.2.com");
        request2.setCharset("UTF-8");
        manager.pushRequest(task, request2);
        boolean await = latch.await(3, TimeUnit.SECONDS);
        Assert.assertTrue("there are not enough message received", await);
        Assert.assertEquals("there are not enough receivers",3,receiver.size());
    }
}
