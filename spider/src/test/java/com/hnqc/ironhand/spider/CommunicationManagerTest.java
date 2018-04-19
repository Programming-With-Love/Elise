package com.hnqc.ironhand.spider;

import com.hnqc.ironhand.spider.message.CommunicationManager;
import com.hnqc.ironhand.spider.message.ThreadCommunicationManager;
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
        this.task = new Site().toTask();
    }

    @Test
    public void testListen() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        CommunicationManager manager = new ThreadCommunicationManager();
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
        manager.start();

        manager.process(task, new Request("www.baidu.com"), new Page());
        manager.download(task, new Request("www.1.com"));
        manager.download(task, new Request("www.1.com"));
        latch.await(3, TimeUnit.SECONDS);
        Assert.assertEquals(0, latch.getCount());
    }
}
