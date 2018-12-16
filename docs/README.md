# Elise 伊莉丝 爬虫框架

![elise](http://pj99lptli.bkt.clouddn.com/assets/logos/elise-logo.png)

> 名字取材于游戏《英雄联盟》中的一名英雄-蜘蛛女皇。

## 简介

Elise一个开源的商业友好的java爬虫框架。他的目标是建立一个强大的可配置分布式爬虫框架，能够囊括绝大多数使用场景，通过大量的基础组建构建，任何人都能够构建一个复杂的爬虫平台

Elise的优势：

* 语义化api，流畅／优雅

* 多线程，速度快

* 自动cookie和会话处理

* 组建化，提供了大量基础组建，扩展性高

* 完全支持分布式

* 提供生命周期回调，任务完成，任务取消,下载成功，下载失败等等一系列回调支持

* 支持任务暂停、取消、恢复等功能，并且配套提供了相应的声明周期回调方法

* 对于爬取结果相较于纯文字或者纯html，增加一种富文本结果支持。富文本能将爬取结果段以文本（带链接文本）+图片+音频+视频等内容通过一个结果呈现，这对于开发者而言也许能更好的做内容爬去，而不再需要费劲解析，完整的文章就完整的保存下来。

* 支持xpath/regex/css多模式组合爬取

* html unit模拟浏览器

* 商业友好（采用 AGPL-3.0 协议）

...

## 模块设计

| 模块名                    | 功能说明                                                                                                                      |
|---------------------------|-------------------------------------------------------------------------------------------------------------------------------|
| Elise-core                | 爬虫框架核心，同时也是爬虫的实现                                                                                              |
| Elise-distributed         | 分布式爬虫扩展，但是不涉及任何具体分布式依赖（例如redis）。仅提供基础类以及相关的接口，其他分布式扩展包必须引用此模块进行扩展 |
| Elise-jedis-support       | 分布式redis支持组件，具体使用jedis                                                                                            |
| Elise-redis-support       | 分布式redis支持组件，具体使用lettuce                                                                                          |
| Elise-kafka-support       | 分布式kafka支持组件                                                                                                           |
| spring-boot-elise-starter | 爬虫的spring boot自动配置组件，用于配合spring boot框架                                                                        |
| ....                      | 更多模块实现，欢迎讨论                                                                                                        |

## maven仓库

```
<dependency>
    <groupId>site.zido</groupId>
    <artifactId>Elise-core</artifactId>
    <version>{version}</version>
</dependency>
```

暂无稳定版本，你可以使用`1.0.0-SNAPSHOT`做为版本号来预先体验开发版本

> 请注意：不稳定且许多api暂未实现，请持续关注[ROADMAP.md](./ROADMAP.md)开发路线文档，期待第一个版本的诞生，一定会惊艳到你

## 快速试用

轻松利用response回调句柄像说话一样简单的爬取一个网站,语义化api，使用绝不迷茫！

优雅的api,让你乐在其中：

> 尝试以舒爽的api爬取我的github仓库吧：

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

框架的核心需编程逻辑仅在response的回调中。response提供了url/html等供你快速的匹配内容

依靠高度封装的api,试着写下`response.`你能轻松的知道接下来可以做什么。

或者转到[使用文档](./TUTORIAL.md)，详细的看看Elise的使用吧

## 构建指北

Elise框架使用maven构建，并使用大量jdk8特性，请保证你的jdk版本为8以上。

获取源码：

`git clone https://github.com/zidoshare/Elise.git`

`cd Elise`

因为作者是使用idea进行开发，所以推荐的开发编辑器为IntelliJ IDEA。

如果你在其他编辑器中开发，请确保你的编辑器中的配置文件/构建文件/缓存等不会出现在提交目录中。这可以在[.gitignore](https://git-scm.com/gitignore)文件中进行设置，也欢迎提交类似的pr

## 状态

进行中（每天都在努力的编码中）...一个人的力量有限，希望有人能加入:smile:

目前还没能达到正式版本的状态。不过已经能基本保证正常运行，可以自行clone代码构建运行。

开发路线参考 [ROADMAP.md](./ROADMAP.md)。

## 贡献

行为准则请参阅[CODE_OF_CONDUCT.md](./CODE_OF_CONDUCT.md)

请查看[贡献说明](./CONTRIBUTING.md)。

## 交流反馈

问题和建议反馈：

[Github issues](https://github.com/zidoshare/Elise/issues)

邮箱: [wuhongxu1208@gmail.com](mailto:wuhongxu1208@gmail.com)

## 感谢

本项目的开发离不开前辈的探索，许多思想理念也来自于其他框架：

* [webmagic](https://github.com/code4craft/webmagic):一个开源的Java垂直爬虫框架.

* [Spiderman2](https://gitee.com/l-weiwei/Spiderman2):简单的说，这是一个网页爬虫工具，专门对网页内容进行抓取和解析

* [colly](https://github.com/gocolly/colly):优雅快速的go语言爬虫框架
