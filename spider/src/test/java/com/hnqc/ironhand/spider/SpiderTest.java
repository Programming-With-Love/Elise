package com.hnqc.ironhand.spider;


import org.junit.Test;

public class SpiderTest {
    @Test
    public void testSpider() {
        Spider.create(new RsPageProcessor()).addUrl("http://renshi.people.com.cn/").thread(5).run();
    }
}
