package com.hnqc.ironhand.common;

import com.hnqc.ironhand.DistributedTask;
import com.hnqc.ironhand.Request;
import com.hnqc.ironhand.Site;
import com.hnqc.ironhand.Spider;
import com.hnqc.ironhand.configurable.ConfigurableUrlFinder;
import com.hnqc.ironhand.configurable.DefExtractor;
import com.hnqc.ironhand.configurable.DefRootExtractor;
import com.hnqc.ironhand.configurable.ExpressionType;
import com.hnqc.ironhand.downloader.HttpClientDownloader;
import com.hnqc.ironhand.pipeline.AbstractSqlPipeline;
import com.hnqc.ironhand.processor.ExtractorPageProcessor;
import com.hnqc.ironhand.scheduler.SimpleTaskScheduler;
import com.hnqc.ironhand.selector.LinkProperty;
import com.hnqc.ironhand.utils.IdWorker;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * com.hnqc.ironhand.common.ConfigurableUrlFinderTest
 *
 * @author zido
 * @date 2018/05/07
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
