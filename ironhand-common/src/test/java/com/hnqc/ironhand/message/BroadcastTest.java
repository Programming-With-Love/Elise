package com.hnqc.ironhand.message;

import com.hnqc.ironhand.common.CommonApplication;
import com.hnqc.ironhand.common.broadcast.BroadcastListener;
import com.hnqc.ironhand.common.broadcast.BroadcastSender;
import com.hnqc.ironhand.common.constants.Status;
import com.hnqc.ironhand.common.pojo.message.BroadcastMessage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonApplication.class)
@Import(BroadcastConfiguration.class)
public class BroadcastTest {
    @Autowired
    private BroadcastSender sender;
    private static CountDownLatch latch = new CountDownLatch(2);
    private final static BroadcastMessage initialMessage;

    static {
        initialMessage = new BroadcastMessage();
        initialMessage.setScheduleId(1L);
        initialMessage.setStatus(Status.FAIL);
    }

    public static class TestHandler implements BroadcastListener.Handler {

        @Override
        public void handle(BroadcastMessage message) {
            if (initialMessage.equals(message)) {
                latch.countDown();
            }
        }
    }

    @Test
    public void testListen() throws InterruptedException {
        sender.send(initialMessage);
        Assert.assertTrue(latch.await(2, TimeUnit.SECONDS));
    }
}
