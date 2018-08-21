package site.zido.elise.common;

import site.zido.elise.DistributedTask;
import site.zido.elise.Request;
import site.zido.elise.Site;
import site.zido.elise.Spider;
import site.zido.elise.configurable.ConfigurableUrlFinder;
import site.zido.elise.configurable.DefExtractor;
import site.zido.elise.configurable.DefRootExtractor;
import site.zido.elise.configurable.ExpressionType;
import site.zido.elise.downloader.HtmlUnitDownloader;
import site.zido.elise.pipeline.AbstractSqlPipeline;
import site.zido.elise.processor.ExtractorPageProcessor;
import site.zido.elise.scheduler.SimpleTaskScheduler;
import site.zido.elise.selector.LinkProperty;
import site.zido.elise.utils.IdWorker;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * ConfigurableUrlFinderTest
 *
 * @author zido
 */
public class ConfigurableUrlFinderTest {
    @Test
    public void testFindInRegion() throws UnsupportedEncodingException, InterruptedException {
        CountDownLatch latch = new CountDownLatch(30);
        Spider spider = new Spider(new SimpleTaskScheduler(), new ExtractorPageProcessor(), new HtmlUnitDownloader(), new AbstractSqlPipeline() {
            @Override
            public void onInsert(String sql, Object[] object) {
                System.out.println(sql + Arrays.toString(object));
                latch.countDown();
            }
        });
        String keyword = URLEncoder.encode("注册会计师", "utf-8");
        DefRootExtractor def = new DefRootExtractor();
        def.addHelpUrl(new ConfigurableUrlFinder().setType(ConfigurableUrlFinder.Type.REGEX).setSourceRegion("//li[@id='sogou_vr_11002301_box_0']").setValue(".*"));
        def.addHelpUrl(new ConfigurableUrlFinder().setValue(".*").setSourceRegion("//div[@class='weui_media_bd']/html()").addLinkProperty(new LinkProperty("h4", "hrefs")));
        def.addTargetUrl(new ConfigurableUrlFinder().setValue("http://mp\\.weixin\\.qq\\.com/s.*"));
        def.setName("hnqc_community.crawl_content");

        def.addChildren(new DefExtractor()
                .setName("title")
                .setType(ExpressionType.XPATH)
                .setValue("//h2[@id='activity-name']/text()")
                .setNullable(false));

        def.addChildren(new DefExtractor()
                .setName("source")
                .setType(ExpressionType.XPATH)
                .setValue("//a[@id='post-user']/text()")
                .setNullable(false));

        def.addChildren(new DefExtractor()
                .setName("text_and_url")
                .setType(ExpressionType.XPATH)
                .setValue("//div[@class='rich_media_content']/html()")
                .setNullable(false));
        spider.start();
        spider.pushRequest(new DistributedTask(IdWorker.nextId(), new Site().setSleepTime(3000).putExtra("downloadMode", "htmlUnit"), def), new Request("http://weixin.sogou.com/weixin?type=1&s_from=input&query=" + keyword));
        latch.await(30000, TimeUnit.SECONDS);
    }
}
