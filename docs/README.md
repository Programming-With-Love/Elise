# 贡献说明

> 注意：非常非常欢迎任何意见或者建议在[issues](https://github.com/zidoshare/Elise/issues)中提出，也可以[发送email给我](mailto:wuhongxu1208@gmail.com)，谢谢

* 开发路线参考 [roadmap.md](./roadmap.md)。

> 如果想要贡献代码，请尽可能阅读并理解本文档，如有问题欢迎讨论，谢谢！

## 构建指北

Elise框架使用maven构建，并使用大量jdk8特性，请保证你的jdk版本为8以上。

获取源码：

`git clone https://github.com/zidoshare/Elise.git`
`cd Elise`

因为作者是使用idea进行开发，所以推荐的开发编辑器为IntelliJ IDEA。

如果你在其他编辑器中开发，请确保你的编辑器中的配置文件/构建文件/缓存等不会出现在提交目录中。这可以在[.gitignore](https://git-scm.com/docs/gitignore)文件中进行设置，也欢迎提交类似的pr

### FAQ

暂无...

## 贡献

> 请务必注意，项目开发分支为dev分支。进行开发时务必在dev分支进行开发。master为稳定分支。

仅接受github的[pull request](https://help.github.com/articles/about-pull-requests/)工作流程。

因作者本人能力水平原因，近期会尽可能的学习github开发指南。也希望能有人提出意见或建议。

## 关于代码

关于代码编写，有以下准则：

### 使用体验很重要，常规使用者绝不接触任何多余api

尽可能让api更流畅。

Spider,Operator均使用接口的方式屏蔽多余的方法，仅留下用户使用的方法。

参考以下逻辑：

* 构造Spider类：`Spider spider = SpiderBuilder.defaults();`

* 用户仅需要关注构造成功的类所提供的方法，分别为：

    爬虫启动类方法：

    `of(ModelExtractor extractor, Config config)`: 能够是用户明白此处需要抓取器和配置

    `of(ModelExtractor extractor)`： 此处不需要任何配置，只需要提供抓取器

    爬虫操作类方法：

    `cancel(boolean ifRunning)`： 取消爬虫

    `boolean pause()`： 暂停爬虫

    `void recover()`： 恢复爬虫

    爬虫事件监听类方法：

    `addEventListener(EventListener listener)`： 添加事件监听器

    `removeEventListener(EventListener listener)`： 删除事件监听器

    实际类的构造由构造器或者实现类内部完成。

* 用户调用Spider.of之后将返回一个Operator接口。他是一个针对任务的操作句柄，并且提供不一样的方法供用户选择调用

  执行类方法：

    `Operator execute(String... url)`： 添加url执行

    `Operator execute(Request request)`： 构建请求执行

  任务操作类方法：

    `cancel(boolean ifRunning)`： 取消本任务

    `boolean pause()`： 暂停本任务

    `void recover()`： 恢复本任务

  爬虫事件监听类方法：

    `addEventListener(EventListener listener)`： 添加事件监听器

  额外提供一个阻塞直到任务完成的方法（有待商榷）：
    `block() throws InterruptedException`：阻塞直到任务完成。

综合以后便得到如下的api使用场景：

```java
//构建Spider实例，用户可通过此实例享有统一配置的爬虫
SpiderBuilder.defaults()
//构建目标爬虫的规则
.of(ExtractorBuilder.create("login").build())
//添加爬虫入口 也可以直接使用execute(url),这里为了尽可能详细展示，使用了RequestBuilder来构建更具体的请求
.execute(RequestBuilder.post("http://xxx").bodyForm("username=xxx&password=xxx").build())
//阻塞任务直到完成
.block();

```

### 编写代码时必须考虑分布式下的场景

本框架在设计时必定遵循的规则是分布式下拥有统一的使用体验并且完全可以向各个方向衍生。

使用ModelExtractor的方式爬去暂时不说，因为是pojo传输可以任意扩展到redis，接下来演示对于将要实现的ResponseHandle api的示例

因暂未实现ResponseHandle api。仅给出以下构想：

```java
//构建分布式爬虫实例
DistributedSpiderBuilder.defaults()
.of("taskName",new ResponseHandler(){
    void onHandle(Response response){
        //url必须匹配到目标表达式才能被确定为是需要采集的页面
        response.url().regex("https://a.b.c").asTarget()
        //url必须匹配到目标表达式才能被确定为是辅助采集的页面
        response.url().regex("https://a.b.c").asHelp()
        //从body中选择
        response.body()
        //使用xpath匹配
        .select(new XpathSelector("xxx"))
        //使用name作为属性名
        .as("name")
        //选中文字作为内容
        .text();
        //从url中选择
        response.url()
        //使用regex匹配
        .select(new RegexSelector("xxx"))
        .nullable(false)
        //使用url作为属性名
        .as("url")
        //选中文字作为内容
        .text();
        //从body中选择
        response.body()
        //使用xpath匹配
        .select(new XpathSelector("xxx"))
        //使用name作为属性名
        .as("description")
        //选中富文本作为内容
        .richText();
    }
})
//添加入口
.execute("https://xxx");
```

对于这部分的实现，是使用模拟真实情况的方式，预计的实现方式是。response其实是一个代理对象，
通过response的各种api最终会生成一个ModelExtractor包含在代理Response内部。然后接下来由框架内部获取这个modelExtractor并实现最终爬取，得到与既定单机实现一样的结果。

## 代码约定

尽可能参照`alibaba开发手册`进行。但是框架本身而言，存在更多的不确定性，所以暂时做出以下两个例外：

* 抽象类不必使用Abstract/Base开头，尽可能的参考本抽象类的功能。例如在httpclient中出现的抽象类`CloseableHttpClient`。

* 字符串不要求提升为`static final`。也可以直接出现代码中，`alibaba`开发手册称之为`魔法字符串`。但是仍然推荐提升。
