package com.hnqc.ironhand;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * UriTest
 *
 * @author zido
 * @date 2018/05/02
 */
public class UriTest {
    @Test
    public void testConvertUrl() throws MalformedURLException {
        String absoluteUrl = "http://www.baidu.com/1/2/3/";
        String relativeUrl = "http://www.baidu.com/6/index.html";
        URL url = new URL(absoluteUrl);
        URL result = new URL(url, relativeUrl);
        System.out.println(result.toString());
    }
}
