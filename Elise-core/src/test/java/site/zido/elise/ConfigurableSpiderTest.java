package site.zido.elise;

import site.zido.elise.configurable.*;
import org.junit.Before;

/**
 * 分布式爬虫测试
 *
 * @author zido
 */
public class ConfigurableSpiderTest {
    private Task task;

    @Before
    public void init() {
        DefRootExtractor def = new DefRootExtractor();
        def.setName("github");
        def.addHelpUrl(new ConfigurableUrlFinder().setValue("https://github\\.com/\\w+\\?tab=repositories"),
                new ConfigurableUrlFinder().setValue("https://github\\.com/\\w+"),
                new ConfigurableUrlFinder().setValue("https://github\\.com/explore/*"));
        def.addTargetUrl(new ConfigurableUrlFinder().setValue("https://github\\.com/\\w+/\\w+"));

        def.addChildren(new DefExtractor("name")
                .setType(ExpressionType.XPATH)
                .setValue("//h1[@class='public']/strong/a/text()")
                .setNullable(false));
        def.addChildren(new DefExtractor("author")
                .setType(ExpressionType.REGEX)
                .setSource(Source.URL)
                .setValue("https://github\\.com/(\\w+)/.*")
                .setNullable(false));
        def.addChildren(new DefExtractor("readme")
                .setType(ExpressionType.XPATH)
                .setValue("//div[@id='readme']/tidyText()")
                .setNullable(false));
        Site site = new Site().setRetryTimes(3).setSleepTime(0);
        task = new DefaultTask(123L, site, def);
    }

}
