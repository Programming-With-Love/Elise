# Elise 伊莉丝 爬虫框架

> 名字取材于游戏《英雄联盟》中的一名英雄-蜘蛛女皇。

## 简介

Elise一个开源的商业友好的java爬虫框架。从功能设计上来说，不论是从单线程到多线程还是从单机到分布式或者从个人到企业，本框架都将能够满足其网络抓取需求。

Elise的优势：

* 流畅、优雅的体验，致力于体验

* 多线程，速度快

* 自动cookie和会话处理

* 扩展性高

* 支持分布式

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

## 状态

进行中（每天都在努力的编码中）...一个人的力量有限，希望有人能加入:smile:

目前还没能达到正式版本的状态。不过已经能基本保证正常运行，可以自行clone代码构建运行。

开发路线参考 [ROADMAP.md](./ROADMAP.md)。

## 贡献

行为准则请参阅[CODE_OF_CONDUCT.md](./CODE_OF_CONDUCT.md)

请查看[贡献说明](./CONTRIBUTING.md)。

## 使用说明

暂无(目前的api可能不稳定,可自行clone探索)

## maven仓库

暂无（不稳定，请期待第一个版本的诞生，一定会惊艳到你）

## 构建指北

Elise框架使用maven构建，并使用大量jdk8特性，请保证你的jdk版本为8以上。

获取源码：

`git clone https://github.com/zidoshare/Elise.git`

`cd Elise`

因为作者是使用idea进行开发，所以推荐的开发编辑器为IntelliJ IDEA。

如果你在其他编辑器中开发，请确保你的编辑器中的配置文件/构建文件/缓存等不会出现在提交目录中。这可以在[.gitignore](https://git-scm.com/docs/gitignore)文件中进行设置，也欢迎提交类似的pr

## 交流反馈

问题和建议反馈：

[Github issues](https://github.com/zidoshare/Elise/issues)

邮箱: [wuhongxu1208@gmail.com](mailto:wuhongxu1208@gmail.com)

## 感谢

本项目的开发离不开前辈的探索，许多思想理念也来自于其他框架：

* [webmagic](https://github.com/code4craft/webmagic):一个开源的Java垂直爬虫框架.

* [Spiderman2](https://gitee.com/l-weiwei/Spiderman2):简单的说，这是一个网页爬虫工具，专门对网页内容进行抓取和解析

* [colly](https://github.com/gocolly/colly):优雅快速的go语言爬虫框架
