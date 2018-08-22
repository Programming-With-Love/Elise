# Elise 伊莉丝 爬虫框架

## 简介

Elise是使用httpclient+jsoup/xsoup封装，由Elise-core模块提供高度可扩展的爬虫框架，支持同步/异步运行，多线程下载，多线程分析等强大的功能
Elise-distributed模块提供了一些分布式扩展，例如基于spring-kafka的消息管理器，基于lettuce封装的使用redis作为远程数据库的url去重管理器。
它完全基于Elise-core扩展，完全不改变任何使用方式。

## 使用说明

框架主要将爬虫分为四个大块，分别是**任务调度器**，**页面处理器**，**下载器**，**结果输出模块**。
其中任务调度器中又由`url去重处理器`和`消息管理器`组成。

个人比较喜欢类组合的形式配置东西，所有很多的模块直接的连接只需要不停的new,new,new就行啦～。

下面是一个较为详细的使用展示
```java
public class SpiderTest{
    public static void main(String[] args){
        Spider.builder(new SimpleTaskScheduler())//基于内存的多线程异步任务调度器, 自动采用基于hash的url去重处理
                .setPageProcessor(new ExtractorPageProcessor())// 使用可配置的页面处理器,使用这个处理器的好处是可以为每个任务定义不同的规则
                // 你也可以自定义处理器，如果你没有为每个url设置不同规则的必要的话
                .setDownloader(new HttpClientDownloader())//使用httpClient下载器
                .addPipeline(new ConsolePipeline())//结果直接输出到控制台
                .build()
                .start()//让爬虫开始异步执行
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
        }
    }
```


## 模块说明

模块名|描述|进度
|:----:|:---:|:----:|
|Elise-core|基本爬虫框架，支持手动编码/xpath/css/regex等多种抓取方式，支持单线程/多线程组合抓取|基本完成|
|Elise-distributed|爬虫基本框架之上提供了分布式支持，主要提供了基于kafka的任务调度器和基于redis的url去重管理器|基本完成|
|...|更多想法，欢迎讨论|随时在线～|

