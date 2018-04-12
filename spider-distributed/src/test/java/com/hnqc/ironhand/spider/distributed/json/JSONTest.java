package com.hnqc.ironhand.spider.distributed.json;

import com.alibaba.fastjson.JSON;
import com.hnqc.ironhand.spider.Request;
import com.hnqc.ironhand.spider.Site;
import com.hnqc.ironhand.spider.Task;
import com.hnqc.ironhand.spider.distributed.configurable.ExpressionType;
import com.hnqc.ironhand.spider.distributed.configurable.ExtractRule;
import com.hnqc.ironhand.spider.distributed.AbstractAsyncDownloader;
import com.hnqc.ironhand.spider.distributed.DsSpiderImpl;
import com.hnqc.ironhand.spider.pipeline.ConsolePipeline;
import com.hnqc.ironhand.spider.scheduler.QueueScheduler;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Date;

public class JSONTest {
    @Test
    public void testDsSpiderImplJson() {
        DsSpiderImpl dsSpider = new DsSpiderImpl(Collections.singletonList(new ConsolePipeline()), new QueueScheduler(), new AbstractAsyncDownloader() {
            @Override
            public void asyncDownload(Request request, Task task) {

            }
        });
        ExtractRule extractRule = new ExtractRule();
        extractRule.setExpressionType(ExpressionType.CSS);
        extractRule.setFieldName("author");
        extractRule.setExpressionParams(new String[]{"dwad"});
        dsSpider.setPageProcessor(new Site().setDomain("www.baidu.com"), Collections.singletonList(extractRule));

        dsSpider.setId(1L);
        dsSpider.setStartTime(new Date());
        dsSpider.setSpawnUrl(false);

        String s = JSON.toJSONString(dsSpider);
        DsSpiderImpl reverseSpider = JSON.parseObject(s, DsSpiderImpl.class);
        reverseSpider.init();
        Assert.assertEquals("author", reverseSpider.getExtractRules().get(0).getFieldName());
    }
}
