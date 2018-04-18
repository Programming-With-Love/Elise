package com.hnqc.ironhand;

import com.hnqc.ironhand.spider.Page;
import com.hnqc.ironhand.spider.Site;
import com.hnqc.ironhand.spider.Spider;
import com.hnqc.ironhand.spider.processor.PageProcessor;

public class LDZLPageProcessor implements PageProcessor {
    @Override
    public void process(Page page) {
        page.addTargetRequests(page.getHtml().links().regex("(http://ldzl\\.people\\.com\\.cn/dfzlk/front/personPage[0-9]+\\.htm$)").all());
        page.putField("title", page.getHtml().xpath("//div[@class='fl p2j_text_center title_2j']/h2[1]/text()").toString());
        page.putField("content", page.getHtml().xpath("//div[@class='fl p2j_text_center title_2j']/div[@class='p2j_text']/tidyText()").toString());
        page.putField("url", page.getUrl().toString());
        if (page.getResultItems().get("title") == null || page.getResultItems().get("content") == null) {
            //skip this page
            page.setSkip(true);
        }
    }
}
