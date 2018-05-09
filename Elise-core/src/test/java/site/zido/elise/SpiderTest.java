package site.zido.elise;

import site.zido.elise.configurable.*;
import site.zido.elise.downloader.HttpClientDownloader;
import site.zido.elise.extractor.ModelExtractor;
import site.zido.elise.pipeline.AbstractSqlPipeline;
import site.zido.elise.scheduler.SimpleTaskScheduler;
import site.zido.elise.pipeline.ConsolePipeline;
import site.zido.elise.processor.ExtractorPageProcessor;
import site.zido.elise.selector.Selectable;
import site.zido.elise.utils.IdWorker;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * SpiderTest
 *
 * @author zido
 * @date 2018/04/19
 */
public class SpiderTest {
    @Test
    public void testRun() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(6);
        Spider spider = new Spider(new SimpleTaskScheduler(),//基于内存的多线程异步任务调度器,
                // 自动采用基于hash的url去重处理
                new ExtractorPageProcessor(),// 使用可配置的页面处理器,
                // 使用这个处理器的好处是可以为每个任务定义不同的规则
                // 你也可以自定义处理器，如果你没有为每个url设置不同规则的必要的话
                new HttpClientDownloader(),// 使用httpClient下载器
                new ConsolePipeline()//控制台打印爬取结果
        ).start()// 爬虫异步准备执行
                .addUrl(new ExtractorTask() {//发送一个任务
                    @Override
                    public ModelExtractor modelExtractor() {//为任务单独定制规则，需要重写ExtractorTask类
                        return new ModelExtractor() {
                            @Override
                            public ResultItem extract(Page page) {
                                String author = page.getUrl().regex("https://github\\.com/(\\w+)/.*").toString();
                                //从html中使用xpath抓取仓库名
                                Selectable name = page.html().xpath("//h1[@class='public']/strong/a/text()");
                                return new ResultItem()
                                        .put("author", author)
                                        .put("name", name)
                                        .setSkip(name == null || author == null);
                                // 如果本页仓库名或作者名为空
                                // 那么本页将不被作为结果输出
                            }

                            @Override
                            public List<String> extractLinks(Page page) {
                                //从页面中的链接中选择挑选可用的链接
                                return page.html().links().regex("(https://github\\.com/zidoshare/[\\w\\-]+)").all();
                            }
                        };
                    }

                    //获取任务id,这个可以不用重写，自动生成id，保证绝对不重复，可以直接作为数据库id
                    @Override
                    public Long getId() {
                        return IdWorker.nextId();
                    }

                    @Override
                    public Site getSite() {
                        return new Site().setRetryTimes(3)//下载失败时重试三次
                                .setSleepTime(1)//每次下载间歇1秒
                                .setCycleRetryTimes(3);// 当下载成功但是处理出问题会重试3次
                    }
                }, "https://github.com/zidoshare");//添加url
        Assert.assertTrue(latch.await(60, TimeUnit.SECONDS));
    }

    @Test
    public void testMulti() throws InterruptedException {
        DefRootExtractor def = new DefRootExtractor();
        def.setName("hnqc_core_member.member_spider");
        def.addTargetUrl(new ConfigurableUrlFinder()
                .setType(ConfigurableUrlFinder.Type.REGEX)
                .setValue("http://ldzl\\.people\\.com\\.cn/dfzlk/front/personPage[0-9]+\\.htm$"));
        def.addHelpUrl(new ConfigurableUrlFinder()
                .setType(ConfigurableUrlFinder.Type.REGEX)
                .setValue("http://ldzl\\.people\\.com\\.cn/dfzlk/front/personPage[0-9]+\\.htm$"));
        def.addHelpUrl(new ConfigurableUrlFinder()
                .setType(ConfigurableUrlFinder.Type.REGEX)
                .setValue("http://ldzl\\.people\\.com\\.cn/dfzlk/front/.*+\\.htm"));
        def.addChildren(new DefExtractor()
                .setName("member_name")
                .setType(ExpressionType.XPATH)
                .setValue("//div[@class='fl p2j_text_center title_2j']/h2[1]/text()"));
        def.addChildren(new DefExtractor()
                .setName("text")
                .setValue("//div[@class='fl p2j_text_center title_2j']/div[@class='p2j_text']/tidyText()")
                .setType(ExpressionType.XPATH));
        def.addChildren(new DefExtractor()
                .setName("origin")
                .setType(ExpressionType.XPATH)
                .setValue("//title/text()"));
        def.addChildren(new DefExtractor()
                .setName("url")
                .setType(ExpressionType.REGEX)
                .setValue("(.*\\.com.cn).*")
                .setSource(Extractor.Source.URL)
                .setNullable(true));

        CountDownLatch latch = new CountDownLatch(20);
        new Spider(new SimpleTaskScheduler(),
                new ExtractorPageProcessor(),
                new HttpClientDownloader(),
                new AbstractSqlPipeline() {
                    @Override
                    public void onInsert(String sql, Object[] object) {
                        System.out.println("sql = [" + sql + "], object = [" + Arrays.toString(object) + "]");
                        latch.countDown();
                    }
                }).start().addUrl(new DistributedTask(IdWorker.nextId(), new Site(), def), "http://ldzl.people.com.cn/dfzlk/front/firstPage.htm");
        latch.await(60, TimeUnit.SECONDS);
    }
}
