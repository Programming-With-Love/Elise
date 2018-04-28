package com.hnqc.coremember.spider;

import com.hnqc.ironhand.DistributedTask;
import com.hnqc.ironhand.Request;
import com.hnqc.ironhand.Site;
import com.hnqc.ironhand.configurable.*;
import com.hnqc.ironhand.schedule.ScheduleClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

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
        client.pushRequest(new DistributedTask(513584385L, new Site(), ldzl), new Request("http://ldzl.people.com.cn/dfzlk/front/firstPage.htm"));
        client.pushRequest(new DistributedTask(513584385L, new Site(), leader), new Request("http://leaders.people.com.cn/GB/70117/index.html"));
        client.pushRequest(new DistributedTask(513584385L, new Site(), rs), new Request("http://renshi.people.com.cn"));
    }

//    @Autowired
//    public void init(ScheduleClient client) {
//        startSpider();
//    }


    private void initExtractor() {
        ldzl.setName("hnqc_core_member.member_spider");
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
                .setName("member_name")
                .setType(ExpressionType.XPATH)
                .setValue("//div[@class='fl p2j_text_center title_2j']/h2[1]/text()"));
        ldzl.addChildren(new DefExtractor()
                .setName("text")
                .setValue("//div[@class='fl p2j_text_center title_2j']/div[@class='p2j_text']/tidyText()")
                .setType(ExpressionType.XPATH));
        ldzl.addChildren(new DefExtractor()
                .setName("origin")
                .setType(ExpressionType.XPATH)
                .setValue("//title/text()"));
        ldzl.addChildren(new DefExtractor()
                .setName("url")
                .setType(ExpressionType.REGEX)
                .setValue(".*")
                .setSource(Extractor.Source.URL)
                .setNullable(true));


        leader.setName("hnqc_core_member.member_spider");
        leader.addTargetUrl(new ConfigurableUrlFinder().setValue("http://leaders\\.people\\.com\\.cn/n1/\\d+/\\d+/c\\d+-\\d+.html$")
                .setType(ConfigurableUrlFinder.Type.REGEX));
        leader.addHelpUrl(new ConfigurableUrlFinder().setValue("http://leaders\\.people\\.com\\.cn/GB/\\d+/index\\d+\\.html")
                .setType(ConfigurableUrlFinder.Type.REGEX));
        leader.addChildren(new DefExtractor()
                .setValue("//div[@class='clearfix w1000_320 text_title']/h1[1]/text()")
                .setName("member_name")
                .setType(ExpressionType.XPATH));
        leader.addChildren(new DefExtractor()
                .setValue("//div[@class='box_con']/tidyText()")
                .setName("text")
                .setType(ExpressionType.XPATH));
        leader.addChildren(new DefExtractor()
                .setName("origin")
                .setType(ExpressionType.XPATH)
                .setValue("//title/text()"));
        leader.addChildren(new DefExtractor()
                .setName("url")
                .setType(ExpressionType.REGEX)
                .setValue(".*")
                .setSource(Extractor.Source.URL)
                .setNullable(true));

        rs.setName("hnqc_core_member.member_spider");
        rs.addTargetUrl(new ConfigurableUrlFinder()
                .setValue("http://renshi\\.people\\.com\\.cn/n1/\\d+/\\d+/[a-z].+\\.html$")
                .setType(ConfigurableUrlFinder.Type.REGEX));
        rs.addHelpUrl(new ConfigurableUrlFinder()
                .setValue("http://renshi\\.people\\.com\\.cn/n1/\\d+/\\d+/[a-z].+\\.html$")
                .setType(ConfigurableUrlFinder.Type.REGEX));
        rs.addChildren(new DefExtractor()
                .setValue("//div[@class='text_c']/h1[1]/text()")
                .setName("member_name")
                .setType(ExpressionType.XPATH));
        rs.addChildren(new DefExtractor()
                .setValue("//div[@class='text_c']/div[@class='show_text']/tidyText()")
                .setName("text")
                .setType(ExpressionType.XPATH));
        rs.addChildren(new DefExtractor()
                .setName("origin")
                .setType(ExpressionType.XPATH)
                .setValue("//title/text()"));
        rs.addChildren(new DefExtractor()
                .setName("url")
                .setType(ExpressionType.REGEX)
                .setValue(".*")
                .setSource(Extractor.Source.URL)
                .setNullable(true));
    }
}
