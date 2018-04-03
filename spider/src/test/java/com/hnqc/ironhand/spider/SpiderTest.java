package com.hnqc.ironhand.spider;


import org.junit.Test;

public class SpiderTest {
    @Test
    public void testSpider() {
        Spider.create(new GithubPageProcessor()).addUrl("https://github.com/zidoshare").thread(5).run();
    }
}
