package site.zido.elise.distributed.sheduler;

import org.junit.Assert;
import org.junit.Test;
import site.zido.elise.Request;
import site.zido.elise.Site;
import site.zido.elise.distributed.scheduler.BlockWaitScheduler;
import site.zido.elise.scheduler.SimpleTaskScheduler;

import java.util.concurrent.CountDownLatch;

public class BlockWaitSchedulerTest {
    @Test
    public void testBlock() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(5);
        SimpleTaskScheduler scheduler = new BlockWaitScheduler(2);
        scheduler.registerDownloader((task, request) -> {
            System.out.println(Thread.currentThread().getName() + " exec analyzer start url : " + request.getUrl());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();
            System.out.println("exec analyzer end url : " + request.getUrl());
        });
        for (int i = 0; i < 2; i++) {
            final int no = i + 1;
            scheduler.pushRequest(new Site().toTask(), new Request(String.valueOf(no)));
            Thread.sleep(333);
        }
        scheduler.pushRequest(new Site().toTask(), new Request("3"));
        Thread.sleep(333);
        Assert.assertTrue(latch.getCount() == 3 || latch.getCount() == 4);
        scheduler.pushRequest(new Site().toTask(), new Request("4"));
        Thread.sleep(333);
        scheduler.pushRequest(new Site().toTask(), new Request("5"));
        Thread.sleep(333);
        latch.await();
    }
}
