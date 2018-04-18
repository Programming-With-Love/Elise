package com.hnqc.ironhand.spider.distributed;

import com.hnqc.ironhand.spider.*;
import com.hnqc.ironhand.spider.distributed.configurable.*;
import com.hnqc.ironhand.spider.distributed.pipeline.MappedPageModelPipeline;
import com.hnqc.ironhand.spider.distributed.pipeline.ModelPipeline;
import com.hnqc.ironhand.spider.processor.ExtractorPageProcessor;
import com.hnqc.ironhand.spider.downloader.HttpClientDownloader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * 分布式爬虫测试
 *
 * @author zido
 * @date 2018/04/16
 */
public class DsSpiderTest {

    private DefRootExtractor def;
    private Task task;

    @Before
    public void init() {
        def = new DefRootExtractor();
        def.setName("github");
        def.addHelpUrl(new ConfigurableUrlFinder().setValue("https://github\\.com/\\w+\\?tab=repositories"),
                new ConfigurableUrlFinder().setValue("https://github\\.com/\\w+"),
                new ConfigurableUrlFinder().setValue("https://github\\.com/explore/*"));
        def.addTargetUrl(new ConfigurableUrlFinder().setValue("https://github\\.com/\\w+/\\w+"));

        def.addChildren(new DefExtractor().setName("name")
                .setType(ExpressionType.XPATH)
                .setValue("//h1[@class='public']/strong/a/text()")
                .setNullable(false));
        def.addChildren(new DefExtractor().setName("author")
                .setType(ExpressionType.REGEX)
                .setSource(Extractor.Source.URL)
                .setValue("https://github\\.com/(\\w+)/.*")
                .setNullable(false));
        def.addChildren(new DefExtractor().setName("readme")
                .setType(ExpressionType.XPATH)
                .setValue("//div[@id='readme']/tidyText()")
                .setNullable(false));
        Site site = new Site().setRetryTimes(3).setSleepTime(0);
        task = new DistributedTask(123L, site);
    }

    @Test
    public void testProcessor() {
        HttpClientDownloader downloader = new HttpClientDownloader();
        Page page = downloader.download(new Request("https://github.com/zidoshare/bone"), task);
        ExtractorPageProcessor processor = new ExtractorPageProcessor(task.getSite(),
                new ConfigurableModelExtractor(def));
        processor.process(page);
        ResultItem resultItem = page.getResultItem();
        Object github = resultItem.get("github");
        if (github instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) github;
            Assert.assertEquals("bone", map.get("name"));
            Assert.assertEquals("zidoshare", map.get("author"));
        }
    }

    @Test
    public void testModelPipeline() {
        HttpClientDownloader downloader = new HttpClientDownloader();
        Page page = downloader.download(new Request("https://github.com/zidoshare/bone"), task);
        ExtractorPageProcessor processor = new ExtractorPageProcessor(task.getSite(),
                new ConfigurableModelExtractor(def));
        processor.process(page);
        MappedPageModelPipeline mappedPipeline = new MappedPageModelPipeline();
        ModelPipeline pipeline = new ModelPipeline();
        pipeline.putPageModelPipeline("github", mappedPipeline);
        pipeline.process(page.getResultItem(), task);
        List<Map<String, Object>> collected = mappedPipeline.getCollected();
        System.out.println(collected.size());
    }
}
