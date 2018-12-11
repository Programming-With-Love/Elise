# 开发路线

API和功能请求应作为PR提交到本文档。

## 框架核心

* (基本完成)完成使用者会接触的各项复杂类的构建类（Builder模式）

* Operator提供`pre(Request)`方法，该方法执行预先请求，主要用于类似登录的操作。提供`follow()`方法提示爬虫将跳转的界面作为入口。

* HtmlUnitDownloader下载器适配以及HtmlUnitDownloaderFactory构建工厂类

* Spider提供的`pause`/`recover`功能实现，提供暂停/恢复功能（已写接口，但并未实现，可查看//TODO)

* proxy代理。目前对于proxy并没有比较好的想法，因为代理的情况很多，考虑很多代理提供商会有`http隧道`和`直接ip`等多种代理方式。目前代理配置了并不会起作用

* 注解配置支持。`spider.of(xxx.class)`方法，读取类注解构建ModelExtractor

* 爬取结果的处理，目前使用Saver的机制，并不能很好的配合使用者，需要进行讨论。

* (已完成)ResponseHandler支持。

    ```java
    SpiderBuilder.defaults().of(response -> {
        response.modelName("project");
        response.asTarget().matchUrl(new LinkSelector("github\\.com/zidoshare/[^/]*$"));
        response.asHelper().filter(new LinkSelector("github\\.com/zidoshare/[^/]*$"));
        response.asContent().html().xpath("//*[@id=\"js-repo-pjax-container\"]/div[1]/div/h1/strong/a").text().save("title");
        response.asContent().html().xpath("//span[@class=\"text-gray-dark mr-2\"]").text().save("description");
        response.asContent().html().xpath("//*[@id=\"readme\"]/div[2]").text().save("readme");
    }).execute("http://github.com/zidoshare").block();
    ```
    api化的构建抓取器

* sleeptime支持，目前的sleeptime仅仅是最简单的实现，实际场景中没有意义，多线程下无法做到等待xxs后执行下一次爬去的场景。所以考虑加入一个全局钟摆实现等待。

* 多线程下异常梳理，有些异常并不能让任务停下来，这个得等到代码进入一定规模后好好梳理一下。

## 分布式

分布式支持还早...