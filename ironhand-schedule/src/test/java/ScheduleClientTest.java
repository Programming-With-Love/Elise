import com.hnqc.ironhand.DistributedTask;
import com.hnqc.ironhand.Request;
import com.hnqc.ironhand.Site;
import com.hnqc.ironhand.configurable.*;
import com.hnqc.ironhand.schedule.ScheduleClient;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ScheduleClientTest
 *
 * @author zido
 * @date 2018/04/27
 */
public class ScheduleClientTest {

    private final static String REDIS_URL = "redisUrl";
    private final static String KAFKA_SERVERS = "kafkaServers";

    @Test
    public void testScheduleClient() throws InterruptedException {
        Properties properties = new Properties();
        try {
            InputStream stream = this.getClass().getResourceAsStream("/config.properties");
            if (stream == null) {
                throw new RuntimeException("config.properties load failed");
            }
            properties.load(stream);
        } catch (IOException e) {
            throw new RuntimeException("加载客户端配置信息失败", e);
        }
        ScheduleClient client = new ScheduleClient(properties.getProperty(KAFKA_SERVERS), properties.getProperty(REDIS_URL));
        client.start();

//        DefRootExtractor def = new DefRootExtractor();
//        def.setName("github");
//        def.addTargetUrl(new ConfigurableUrlFinder().setValue("(https://github\\.com/zidoshare/[\\w\\-]+)").setType(ConfigurableUrlFinder.Type.REGEX));
//        def.addHelpUrl(new ConfigurableUrlFinder().setValue("https://github\\.com/zidoshare.*+").setType(ConfigurableUrlFinder.Type.REGEX));
//        def.addChildren(new DefExtractor()
//                .setName("author")
//                .setType(ExpressionType.REGEX)
//                .setSource(Extractor.Source.URL)
//                .setValue("https://github\\.com/(\\w+)/.*"));
//
//        def.addChildren(new DefExtractor()
//                .setName("name")
//                .setValue("//h1[@class='public']/strong/a/text()")
//                .setType(ExpressionType.XPATH));
//        client.pushRequest(new DistributedTask(40L, new Site().setCycleRetryTimes(3), def), new Request("https://github.com/zidoshare"));
//        Thread.sleep(10 * 1000);


        DefRootExtractor def = new DefRootExtractor();
        def.setName("test.renshi");
        def.addTargetUrl(new ConfigurableUrlFinder().setValue("http://ldzl\\.people\\.com\\.cn/dfzlk/front/personPage[0-9]+\\.htm$").setType(ConfigurableUrlFinder.Type.REGEX));
        def.addChildren(new DefExtractor()
                .setName("title")
                .setType(ExpressionType.XPATH)
                .setValue("//div[@class='fl p2j_text_center title_2j']/h2[1]/text()"));

        def.addChildren(new DefExtractor()
                .setName("content")
                .setValue("//div[@class='fl p2j_text_center title_2j']/div[@class='p2j_text']/tidyText()")
                .setType(ExpressionType.XPATH));
        def.addChildren(new DefExtractor()
                .setName("url")
                .setValue(".*")
                .setType(ExpressionType.REGEX)
                .setSource(Extractor.Source.URL));
        DistributedTask task = new DistributedTask(1L, new Site().setCycleRetryTimes(3), def);
        client.clearDuplications(task);
        client.pushRequest(task,
                new Request("http://ldzl.people.com.cn/dfzlk/front/personIndex.htm"));
        client.pushRequest(task,
                new Request("http://ldzl.people.com.cn/dfzlk/front/fusheng.htm"));
        client.pushRequest(task,
                new Request("http://ldzl.people.com.cn/dfzlk/front/xian35.htm"));
        Thread.sleep(10 * 1000);
    }
}
