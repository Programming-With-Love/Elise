package site.zido.elise.spider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import site.zido.elise.DistributedTask;
import site.zido.elise.Request;
import site.zido.elise.Site;
import site.zido.elise.configurable.*;
import site.zido.elise.schedule.ScheduleClient;

import java.util.ArrayList;
import java.util.List;

/**
 * SpiderConfiguration
 *
 * @author zido
 * @date 2018/04/27
 */
@Configuration
public class SpiderConfiguration {
    @ConfigurationProperties
    public class Props {
        private String redisServer;
        private String kafkaServers;
        private String groupId;
        private String topicAnalyzer;
        private String topicDownloader;

        public Props setRedisServer(String redisServer) {
            this.redisServer = redisServer;
            return this;
        }

        public Props setKafkaServers(String kafkaServers) {
            this.kafkaServers = kafkaServers;
            return this;
        }

        public Props setGroupId(String groupId) {
            this.groupId = groupId;
            return this;
        }

        public Props setTopicAnalyzer(String topicAnalyzer) {
            this.topicAnalyzer = topicAnalyzer;
            return this;
        }

        public Props setTopicDownloader(String topicDownloader) {
            this.topicDownloader = topicDownloader;
            return this;
        }
    }

    private ScheduleClient client;
    private DefRootExtractor rs;

    private List<String> keywords = new ArrayList<>();


    public SpiderConfiguration() {
        rs = new DefRootExtractor();
        initExtractor();
    }

    @Bean
    public Props props() {
        return new Props();
    }

    @Bean
    @Autowired
    public ScheduleClient spider(Props props) {
        client = new ScheduleClient(props.kafkaServers, props.redisServer, props.groupId, props.topicAnalyzer, props.topicDownloader);
        client.start();
        return client;
    }

    @Scheduled(cron = "0 0 1 1 * ?")
    public void startSpider() {
        client.pushRequest(new DistributedTask(513584386L, new Site(), rs), new Request("http://renshi.people.com.cn"));
    }

    private void initExtractor() {
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
}
