package site.zido.elise.distributed;

import site.zido.elise.DefaultTask;
import site.zido.elise.Request;
import site.zido.elise.Site;
import site.zido.elise.scheduler.DuplicationProcessor;
import org.junit.Assert;
import org.junit.Test;

/**
 * RedisDuplicationProcessorTest
 *
 * @author zido
 */
public class RedisDuplicationProcessorTest {
    @Test
    public void testRedis() {
        DefaultTask task = new DefaultTask(1L, new Site(), null);
        DefaultTask task2 = new DefaultTask(2L, new Site(), null);
        DuplicationProcessor processor = new SimpleRedisDuplicationProcessor("redis://123456@localhost:6379/0");
        processor.resetDuplicateCheck(task);
        processor.resetDuplicateCheck(task2);
        try {
            Assert.assertFalse(processor.isDuplicate(new Request("www.baidu.com"), task));
            Assert.assertEquals(1, processor.getTotalRequestsCount(task));
            Assert.assertFalse(processor.isDuplicate(new Request("www.sohu.com"), task));
            Assert.assertTrue(processor.isDuplicate(new Request("www.baidu.com"), task));
            Assert.assertFalse(processor.isDuplicate(new Request("www.google.com"), task));
            Assert.assertFalse(processor.isDuplicate(new Request("www.baidu.com"),
                    task2));
            Assert.assertEquals(3, processor.getTotalRequestsCount(task));
        } finally {
            processor.resetDuplicateCheck(task);
            processor.resetDuplicateCheck(task2);
        }
    }
}
