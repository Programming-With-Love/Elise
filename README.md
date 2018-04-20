# 铁手  分布式爬取系统

## 简介

ironhand是使用httpclient+jsoup/xsoup封装，由ironhand-core模块提供高度可扩展的爬虫框架，支持同步/异步运行，多线程下载，多线程分析等强大的功能
ironhand-extension模块提供了一些分布式扩展，例如基于spring-kafka的消息管理器，基于spring-boot-starter-data-redis封装的分布式去重管理器。
它完全基于ironhand-core扩展，完全不改变任何使用方式。它的核心灵感来源于webmagic框架，他们拥有较为类似的抓取机制，但是ironhand提供了更加详细的模块以及更高的扩展能力

ironhand-downlodaer和ironhand-analyzer模块是可以单独部署的下载和分析客户端，使用了kafka/redis/jdbc等机制，所以需要做相应的环境准备。
ironhand-schedule是需要被集成的任务发送端。

## 模块说明

模块名|描述|进度
|:----:|:---:|:----:|
|ironhand-core|基本爬虫框架，支持手动编码/xpath/css/regex等多种抓取方式，支持单线程/多线程组合抓取。支持ip代理|基本完成|
|ironhand-extension|爬虫基本框架之上提供了分布式支持|基本完成|
|ironhand-downloader|分布式爬虫下载客户端，用于下载内容|基本完成|
|ironhand-analyzer|分布式爬虫分析客户端，用于从页面中抓取需要的信息|基本完成|
|ironhand-schedule|分布式爬虫任务发送端|基本完成|
