package com.hnqc.coremember.spider;

import com.hnqc.coremember.spider.service.MemberService;
import com.hnqc.ironhand.DistributedTask;
import com.hnqc.ironhand.Request;
import com.hnqc.ironhand.Site;
import com.hnqc.ironhand.Task;
import com.hnqc.ironhand.configurable.*;
import com.hnqc.ironhand.schedule.ScheduleClient;
import com.hnqc.ironhand.selector.LinkProperty;
import com.hnqc.ironhand.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * SpiderConfiguration
 *
 * @author zido
 * @date 2018/04/27
 */
@Configuration
@ConfigurationProperties
public class SpiderConfiguration {
    @Value("${redisUrl}")
    private String redisServer;
    @Value("${kafkaServers}")
    private String kafkaServer;
    private ScheduleClient client;
    private DefRootExtractor ldzl;
    private DefRootExtractor leader;
    private DefRootExtractor rs;

    private MemberService memberService;

    private List<String> keywords = new ArrayList<>();


    public SpiderConfiguration() {
        ldzl = new DefRootExtractor();
        leader = new DefRootExtractor();
        rs = new DefRootExtractor();
        initExtractor();
    }

    @Bean
    public ScheduleClient spider() {
        client = new ScheduleClient(kafkaServer, redisServer);
        client.start();
        return client;
    }

    @Scheduled(cron = "0 0 1 1 * ?")
    public void startSpider() {
        client.pushRequest(new DistributedTask(513584386L, new Site(), ldzl), new Request("http://ldzl.people.com.cn/dfzlk/front/firstPage.htm"));
        client.pushRequest(new DistributedTask(513584386L, new Site(), leader), new Request("http://leaders.people.com.cn/GB/70117/index.html"));
        client.pushRequest(new DistributedTask(513584386L, new Site(), rs), new Request("http://renshi.people.com.cn"));
    }

    @Scheduled(cron = "0 30 1,3,5,8,12 1,2,3 * ?")
    public void startHandleCoreData() {
        List<String> names = memberService.findMemberNames();
        for (String name : names) {
            memberService.moveToSpiderByName(name);
        }

        List<String> keyWords = memberService.findKeyWords();
        memberService.moveToSpiderByKeyWord(keyWords, "陌生校友");
    }

    /**
     * 每周日的晚上22点执行爬取
     */
    @Scheduled(cron = "0 0 22 ? * SUN")
    public void startSpider2() {
        try {
            keywords.add(URLEncoder.encode("李叫兽", "utf-8"));
            keywords.add(URLEncoder.encode("老苗撕营销", "utf-8"));
            keywords.add(URLEncoder.encode("营销界的007", "utf-8"));
            keywords.add(URLEncoder.encode("西蒙官人", "utf-8"));
            keywords.add(URLEncoder.encode("休克文案", "utf-8"));
            keywords.add(URLEncoder.encode("TopMarketing", "utf-8"));
            keywords.add(URLEncoder.encode("销售与市场", "utf-8"));
            keywords.add(URLEncoder.encode("公关界的007", "utf-8"));

            keywords.add(URLEncoder.encode("管理会计", "utf-8"));
            keywords.add(URLEncoder.encode("注册会计师", "utf-8"));
            keywords.add(URLEncoder.encode("高顿金融分析师", "utf-8"));
            keywords.add(URLEncoder.encode("中国管理会计网", "utf-8"));
            keywords.add(URLEncoder.encode("普华永道中国", "utf-8"));
            keywords.add(URLEncoder.encode("马靖昊说会计", "utf-8"));
            keywords.add(URLEncoder.encode("大叔说会计", "utf-8"));

            keywords.add(URLEncoder.encode("人力葵花", "utf-8"));
            keywords.add(URLEncoder.encode("百场汇", "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        DefRootExtractor def = new DefRootExtractor();
        def.addHelpUrl(new ConfigurableUrlFinder().setType(ConfigurableUrlFinder.Type.REGEX).setSourceRegion("//li[@id='sogou_vr_11002301_box_0'").setValue(".*"));
        def.addHelpUrl(new ConfigurableUrlFinder().setValue(".*").setSourceRegion("//div[@class='weui_media_bd']/html()").addLinkProperty(new LinkProperty("h4", "hrefs")));
        def.addTargetUrl(new ConfigurableUrlFinder().setValue("http://mp\\.weixin\\.qq\\.com/s.*"));
        def.setName("hnqc_community.crawl_content");

        def.addChildren(new DefExtractor()
                .setName("title")
                .setType(ExpressionType.XPATH)
                .setValue("//h2[@id='activity-name']/text()")
                .setNullable(false));

        def.addChildren(new DefExtractor()
                .setName("source")
                .setType(ExpressionType.XPATH)
                .setValue("//a[@id='post-user']/text()")
                .setNullable(false));

        def.addChildren(new DefExtractor()
                .setName("textAndUrl")
                .setType(ExpressionType.XPATH)
                .setValue("//div[@class='rich_media_content']/html()")
                .setNullable(false));
        for (String keyword : keywords) {
            client.pushRequest(new DistributedTask(IdWorker.nextId(), new Site().putExtra("downloadMode", "htmlUnit"), def), new Request("http://weixin.sogou.com/weixin?type=1&s_from=input&query=" + keyword));
        }
    }

    @Autowired
    public void init(ScheduleClient client) {
        startHandleCoreData();
    }


    private void initExtractor() {
        ldzl.setName("hnqc_core_member.member_spider_tmp");
        ldzl.addTargetUrl(new ConfigurableUrlFinder()
                .setType(ConfigurableUrlFinder.Type.REGEX)
                .setValue("http://ldzl\\.people\\.com\\.cn/dfzlk/front/personPage[0-9]+\\.htm$"));
        ldzl.addHelpUrl(new ConfigurableUrlFinder()
                .setType(ConfigurableUrlFinder.Type.REGEX)
                .setValue("http://ldzl\\.people\\.com\\.cn/dfzlk/front/personPage[0-9]+\\.htm$"));
        ldzl.addHelpUrl(new ConfigurableUrlFinder()
                .setType(ConfigurableUrlFinder.Type.REGEX)
                .setValue("http://ldzl\\.people\\.com\\.cn/dfzlk/front/.*+\\.htm"));
        ldzl.addChildren(new DefExtractor()
                .setNullable(false)
                .setName("title")
                .setType(ExpressionType.XPATH)
                .setValue("/html/body/div[4]/div[2]/ul/li/dl/dd/div/em/text()"));
        ldzl.addChildren(new DefExtractor()
                .setNullable(false)
                .setName("text")
                .setValue("//div[@class='fl p2j_text_center title_2j']/div[@class='p2j_text']/tidyText()")
                .setType(ExpressionType.XPATH));
        ldzl.addChildren(new DefExtractor()
                .setNullable(false)
                .setName("origin")
                .setType(ExpressionType.XPATH)
                .setValue("//title/text()"));
        ldzl.addChildren(new DefExtractor()
                .setNullable(false)
                .setName("url")
                .setType(ExpressionType.REGEX)
                .setValue(".*")
                .setSource(Extractor.Source.URL)
                .setNullable(true));


        leader.setName("hnqc_core_member.member_spider_tmp");
        leader.addTargetUrl(new ConfigurableUrlFinder().setValue("http://leaders\\.people\\.com\\.cn/n1/\\d+/\\d+/c\\d+-\\d+.html$")
                .setType(ConfigurableUrlFinder.Type.REGEX));
        leader.addHelpUrl(new ConfigurableUrlFinder().setValue("http://leaders\\.people\\.com\\.cn/GB/\\d+/index\\d+\\.html")
                .setType(ConfigurableUrlFinder.Type.REGEX));
        leader.addChildren(new DefExtractor()
                .setNullable(false)
                .setValue("//div[@class='clearfix w1000_320 text_title']/h1[1]/text()")
                .setName("title")
                .setType(ExpressionType.XPATH));
        leader.addChildren(new DefExtractor()
                .setNullable(false)
                .setValue("//div[@class='box_con']/tidyText()")
                .setName("text")
                .setType(ExpressionType.XPATH));
        leader.addChildren(new DefExtractor()
                .setNullable(false)
                .setName("origin")
                .setType(ExpressionType.XPATH)
                .setValue("//title/text()"));
        leader.addChildren(new DefExtractor()
                .setNullable(false)
                .setName("url")
                .setType(ExpressionType.REGEX)
                .setValue(".*")
                .setSource(Extractor.Source.URL)
                .setNullable(true));

        rs.setName("hnqc_core_member.member_spider_tmp");
        rs.addTargetUrl(new ConfigurableUrlFinder()
                .setValue("http://renshi\\.people\\.com\\.cn/n1/\\d+/\\d+/[a-z].+\\.html$")
                .setType(ConfigurableUrlFinder.Type.REGEX));
        rs.addHelpUrl(new ConfigurableUrlFinder()
                .setValue("http://renshi\\.people\\.com\\.cn/n1/\\d+/\\d+/[a-z].+\\.html$")
                .setType(ConfigurableUrlFinder.Type.REGEX));
        rs.addChildren(new DefExtractor()
                .setNullable(false)
                .setValue("//div[@class='text_c']/h1[1]/text()")
                .setName("title")
                .setType(ExpressionType.XPATH));
        rs.addChildren(new DefExtractor()
                .setNullable(false)
                .setValue("//div[@class='text_c']/div[@class='show_text']/tidyText()")
                .setName("text")
                .setType(ExpressionType.XPATH));
        rs.addChildren(new DefExtractor()
                .setNullable(false)
                .setName("origin")
                .setType(ExpressionType.XPATH)
                .setValue("//title/text()"));
        rs.addChildren(new DefExtractor()
                .setNullable(false)
                .setName("url")
                .setType(ExpressionType.REGEX)
                .setValue(".*")
                .setSource(Extractor.Source.URL)
                .setNullable(true));
    }

    @Autowired
    public SpiderConfiguration setMemberService(MemberService memberService) {
        this.memberService = memberService;
        return this;
    }
}
