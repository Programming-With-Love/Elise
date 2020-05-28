package site.zido.elise.client;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import site.zido.elise.Operator;
import site.zido.elise.Spider;
import site.zido.elise.events.SingleEventListener;
import site.zido.elise.processor.BlankSaver;
import site.zido.elise.processor.MemorySaver;
import site.zido.elise.processor.ResultItem;
import site.zido.elise.scheduler.NoDuplicationProcessor;
import site.zido.elise.select.CssSelector;
import site.zido.elise.task.api.PartitionDescriptor;
import site.zido.elise.test.Server;
import site.zido.elise.utils.SystemClock;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static site.zido.elise.test.Server.MULTI_PATH_ENTRY;
import static site.zido.elise.test.Server.ONE_PATH;

/**
 * SpiderTest
 *
 * @author zido
 */
public class SpiderTest {
    private Server server;

    @Before
    public void startServer() {
        server = new Server();
        server.start();
    }

    @After
    public void stopServer() {
        server.stop();
    }

    /**
     * 简单单页爬取测试
     */
    @Test
    public void testOnePage() throws InterruptedException {
        MemorySaver saver = new MemorySaver(System.out);
        final boolean[] ifSuccess = {false};
        //构造爬虫
        //爬虫默认为异步运行
        Spider spider = SpiderBuilder
            .create()
            .setDuplicationProcessor(new NoDuplicationProcessor())
            .setSaver(saver)
            .build();
        //为抓取器部署抓取任务
        spider.of(response -> {
            response.modelName("blog");
            response.target().matchUrl(".+");
            response.content().url().name("source_url");
            PartitionDescriptor partition = response.partition(new CssSelector(".list-item"));
            partition.field().css("h1").text().name("title");
            partition.field().css("p").text().name("description");
            //获取任务操作句柄后添加一个事件监听器
        }).addEventListener(new SingleEventListener() {
            @Override
            public void onSuccess() {
                ifSuccess[0] = true;
            }
            //为该任务添加一个入口
        }).execute(ONE_PATH)
            //阻塞直到爬虫完成任务
            .block();
        Assert.assertTrue(ifSuccess[0]);
        Map<Long, List<ResultItem>> cup = saver.getCup();
        Set<Long> keys = cup.keySet();
        Assert.assertEquals(1, keys.size());
        Long key = null;
        for (Long k : keys) {
            key = k;
        }
        List<ResultItem> resultItems = cup.get(key);
        Assert.assertEquals(3, resultItems.size());
        String[] expectedTitles = {"title first in one", "title second in one", "title third in one"};
        String[] expectedDescriptions = {"这是《title first in one》的描述文本", "这是《title second in one》的描述文本", "这是《title third in one》的描述文本"};
        int index = 0;
        for (ResultItem resultItem : resultItems) {
            String title = (String) ((List) resultItem.get("title")).get(0);
            String description = (String) ((List) resultItem.get("description")).get(0);
            Assert.assertEquals(expectedTitles[index], title);
            Assert.assertEquals(expectedDescriptions[index++], description);
        }
    }

    @Test
    public void testWithMultiPage() throws InterruptedException {
        MemorySaver saver = new MemorySaver(System.out);
        Spider spider = SpiderBuilder.create().setSaver(saver).build();
        spider.of(response -> {
            response.modelName("blog");
            response.target().matchUrl("/multi/");
            response.helper().regex("/multi/");
            response.content().html().css(".content>h1").text().name("title").nullable(false);
            response.content().html().css(".content>p").text().name("description").nullable(false);
            response.content().url().name("source_url");
        }).execute(MULTI_PATH_ENTRY).block();
        Map<Long, List<ResultItem>> cup = saver.getCup();
        Set<Long> keys = cup.keySet();
        Assert.assertEquals(1, keys.size());
        Long key = null;
        for (Long k : keys) {
            key = k;
        }
        List<ResultItem> resultItems = cup.get(key);
        Assert.assertEquals(3, resultItems.size());
    }

    @Test
    public void testCancel() throws InterruptedException {
        System.out.println("start in " + SystemClock.now());
        AtomicInteger target = new AtomicInteger(0);
        Spider spider = SpiderBuilder.create().setSaver(new BlankSaver()).build();
        Operator operator = spider.of(response -> {
            response.modelName("project");
            response.target().matchUrl("github\\.com/zidoshare/[^/]*$");
            response.helper().regex("github\\.com/zidoshare/[^/]*$");
            response.content().html().xpath("//*[@id=\"js-repo-pjax-container\"]/div[1]/div/h1/strong/a").text().name("title");
            response.content().html().xpath("//span[@class=\"text-gray-dark mr-2\"]").text().name("description");
            response.content().html().xpath("//*[@id=\"readme\"]/div[2]").text().name("readme");
            response.content().url().name("source_url");
        }).execute("http://github.com/zidoshare")
            .addEventListener(new SingleEventListener() {
                @Override
                public void onSuccess() {
                    System.out.println("success in " + SystemClock.now());
                    target.incrementAndGet();
                }

                @Override
                public void onRecover() {
                    System.out.println("recover in " + SystemClock.now());
                    target.incrementAndGet();
                }

                @Override
                public void onPause() {
                    System.out.println("pause in " + SystemClock.now());
                    target.incrementAndGet();
                }
            });
        Thread.sleep(1000);
        spider.pause();
        Thread.sleep(3000);
        spider.recover();
        operator.block();
        Assert.assertEquals(3, target.get());
    }
}
