package com.hnqc.ironhand.message;

import com.hnqc.ironhand.common.CommonApplication;
import com.hnqc.ironhand.common.message.BroadcastListener;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonApplication.class)
public class BroadcastTest {
    @Autowired
    private KafkaTemplate<String, String> template;
    @Test
    public void testListen() throws InterruptedException {
        template.send("broadcast", "这是一条广播消息");
        Assert.assertTrue(BroadcastListener.latch.await(2, TimeUnit.SECONDS));
    }
}
