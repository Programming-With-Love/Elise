package com.hnqc.ironhand.spider;

import com.hnqc.ironhand.spider.processor.PageProcessor;

public class RsPageProcessor implements PageProcessor {
    @Override
    public void process(Page page) {
        page.addTargetRequests(page.getHtml().links().regex("(http://renshi.people.com.cn/n1/\\d+/\\d+/[a-z].+\\.html$)").all());
        page.putField("title", page.getHtml().xpath("//div[@class='text_c']/h1[1]/text()").toString());
        page.putField("content", page.getHtml().xpath("//div[@class='text_c']/div[@class='show_text']/tidyText()"));
        if (page.getResultItems().get("title") == null || page.getResultItems().get("content") == null) {
            //skip this page
            page.setSkip(true);
        }
    }

    @Override
    public Site getSite() {
        return new Site().setRetryTimes(3);
    }
}
