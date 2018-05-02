package com.hnqc.ironhand.configurable;

import com.hnqc.ironhand.Page;
import com.hnqc.ironhand.selector.LinkProperty;
import com.hnqc.ironhand.selector.PlainText;
import com.hnqc.ironhand.selector.Selector;
import com.hnqc.ironhand.selector.UrlFinderSelector;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ConfigurableModelExtractorTest
 *
 * @author zido
 * @date 2018/05/02
 */
public class ConfigurableModelExtractorTest {
    private String html = null;

    @Before
    public void importTemplate() throws IOException, URISyntaxException {
        URL resource = getClass().getResource("/test.html");
        File file = new File(resource.toURI());
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuilder htmlBuilder = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            htmlBuilder.append(line);
        }
        html = htmlBuilder.toString();
    }

    @Test
    public void testExtractorLinks() {
        Page page = new Page().setRawText(html);
        page.setUrl(new PlainText("https://mp.weixin.qq.com/profile?src=3&timestamp=1525237719&ver=1&signature=N5z0SZ4HiuhWXJvPynWiu8bm2uWTCcrDvebAly28lh0iEqDRuzvohrPyjkpUM0y*ODgEOgPwq5HujBsyDC0y5w=="));
        List<UrlFinderSelector> selectors = new ArrayList<>();
        selectors.add(new UrlFinderSelector(".*",
                ConfigurableUrlFinder.Type.REGEX,
                "//div[@class='weui_media_bd']/html()")
        .setLinkProperties(Collections.singletonList(new LinkProperty("h4", "hrefs"))));
        List<String> links = new ArrayList<>();
        for (UrlFinderSelector selector : selectors) {
            links.addAll(page.html().selectList(selector).all());
        }
        //兜底链接处理
        links = links.stream().map(link -> {
            link = link.replace("&amp;", "&");
            if (link.startsWith("http")) {
                //已经是绝对路径的，不再处理
                return link;
            }
            try {
                return new URL(new URL(page.getUrl().toString()), link).toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return link;
        }).collect(Collectors.toList());
        System.out.println(Arrays.toString(links.toArray()));
    }
}
