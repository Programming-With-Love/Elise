package com.hnqc.ironhand;

import com.hnqc.ironhand.spider.Page;
import com.hnqc.ironhand.spider.Site;
import com.hnqc.ironhand.spider.processor.PageProcessor;

public class LeadersPageProcessor implements PageProcessor {
    @Override
    public void process(Page page) {
        page.addTargetRequests(page.getHtml().links().regex("(http://leaders.people.com.cn/n1/\\d+/\\d+/c\\d+-\\d+.html$)").all());
        page.addTargetRequests(page.getHtml().links().regex("(http://leaders\\.people\\.com\\.cn/GB/\\d+/index\\d+\\.html$)").all());
        page.putField("title", page.getHtml().xpath("//div[@class='clearfix w1000_320 text_title']/h1[1]/text()").toString());
        page.putField("content", page.getHtml().xpath("//div[@class='box_con']/tidyText()").toString());
        page.putField("url", page.getUrl().toString());
        if (page.getResultItems().get("title") == null || page.getResultItems().get("content") == null) {
            //skip this page
            page.setSkip(true);
        }
    }
}
