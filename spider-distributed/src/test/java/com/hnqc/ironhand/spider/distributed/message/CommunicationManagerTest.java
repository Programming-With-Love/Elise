package com.hnqc.ironhand.spider.distributed.message;

import com.hnqc.ironhand.spider.Page;
import com.hnqc.ironhand.spider.Request;
import com.hnqc.ironhand.spider.Site;
import com.hnqc.ironhand.spider.Task;
import com.hnqc.ironhand.spider.distributed.DistributedTask;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * CommunicationManagerTest
 *
 * @author zido
 * @date 2018/04/17
 */
public class CommunicationManagerTest {
    private Task task;

    @Before
    public void setUp() {
        this.task = new DistributedTask(1L, new Site());
    }

    @Test
    public void testListen() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        CommunicationManager manager = new ThreadCommunicationManager();
        manager.registerAnalyzer((task, request, page, extractor) -> {
            Assert.assertEquals(1L, task.getId().longValue());
            Assert.assertEquals("www.baidu.com", request.getUrl());
            latch.countDown();
        });

        manager.registerDownloader((task, request, extractor) -> {
            Assert.assertEquals(1L, task.getId().longValue());
            Assert.assertEquals("www.1.com", request.getUrl());
            latch.countDown();
        });

        CommunicationManager manager2 = new ThreadCommunicationManager();
        manager2.registerDownloader((task, request, extractor) -> {
            Assert.assertEquals(1L, task.getId().longValue());
            Assert.assertEquals("www.1.com", request.getUrl());
            latch.countDown();
        });

        CommunicationManager sender = new ThreadCommunicationManager();
        sender.process(task, new Request("www.baidu.com"), new Page(), null);
        sender.download(task, new Request("www.1.com"), null);
        sender.download(task, new Request("www.1.com"), null);
        latch.await(3, TimeUnit.SECONDS);
    }
}
