package com.hnqc.ironhand;

import com.hnqc.ironhand.spider.Page;
import com.hnqc.ironhand.spider.Site;
import com.hnqc.ironhand.spider.Spider;
import com.hnqc.ironhand.spider.pipeline.MysqlPipeline;
import com.hnqc.ironhand.spider.processor.PageProcessor;
import com.hnqc.ironhand.utils.IdWorker;

public class RsPageProcessor implements PageProcessor {
    @Override
    public void process(Page page) {
        page.addTargetRequests(page.getHtml().links().regex("(http://renshi.people.com.cn/n1/\\d+/\\d+/[a-z].+\\.html$)").all());
        page.putField("title", page.getHtml().xpath("//div[@class='text_c']/h1[1]/text()").toString());
        page.putField("content", page.getHtml().xpath("//div[@class='text_c']/div[@class='show_text']/tidyText()").toString());
        page.putField("url", page.getUrl().toString());
        if (page.getResultItems().get("title") == null || page.getResultItems().get("content") == null) {
            //skip this page
            page.setSkip(true);
        }
    }

    public static void main(String[] args) {
        Spider.create(new LDZLPageProcessor())
                .setId(1L)
                .addPipeline(new MysqlPipeline("result", IdWorker::nextId))
                .addUrl("http://ldzl.people.com.cn/dfzlk/front/personIndex.htm",
                        "http://ldzl.people.com.cn/dfzlk/front/fusheng.htm",
                        "http://ldzl.people.com.cn/dfzlk/front/xian35.htm")
                .thread(5)
                .start();

        Spider.create(new LeadersPageProcessor())
                .setId(2L)
                .addPipeline(new MysqlPipeline("result", IdWorker::nextId))
                .addUrl("http://leaders.people.com.cn/GB/70117/index.html",
                        "http://leaders.people.com.cn/GB/70149/index.html")
                .thread(5)
                .start();

        Spider.create(new RsPageProcessor())
                .setId(4L)
                .addPipeline(new MysqlPipeline("result", IdWorker::nextId))
                .addUrl("http://renshi.people.com.cn")
                .thread(5)
                .start();
    }
}
