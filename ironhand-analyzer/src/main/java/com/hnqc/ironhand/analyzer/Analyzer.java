package com.hnqc.ironhand.analyzer;

import com.hnqc.ironhand.common.constants.Rule;
import com.hnqc.ironhand.common.pojo.UrlEntry;
import com.hnqc.ironhand.common.pojo.UrlRule;
import com.hnqc.ironhand.common.pojo.entity.ContentResult;
import com.hnqc.ironhand.common.pojo.entity.Scheduler;
import com.hnqc.ironhand.common.pojo.entity.Seed;
import com.hnqc.ironhand.common.query.RuleFactory;
import com.hnqc.ironhand.common.query.Ruler;
import com.hnqc.ironhand.common.query.UrlQuery;
import com.hnqc.ironhand.common.repository.SchedulerRepository;
import com.hnqc.ironhand.common.sender.DownLoadSender;
import com.hnqc.ironhand.common.service.IFileService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class Analyzer {
    private SchedulerRepository schedulerRepository;
    private IFileService fileService;
    private DownLoadSender downLoadSender;
    private Logger logger = LoggerFactory.getLogger(Analyzer.class);

    public void analysis(Seed seed) {
        Long schedulerId = seed.getSchedulerId();
        Optional<Scheduler> optional = schedulerRepository.findById(schedulerId);
        optional.ifPresent(scheduler -> {
            List<UrlQuery> urlQueries = scheduler.getUrlQueries();
            //遍历查询条件
            for (UrlQuery urlQuery : urlQueries) {
                //遍历seed带来的入口
                for (UrlEntry urlEntry : seed.getUrls()) {
                    //如果匹配到
                    if (matchRule(urlQuery, urlEntry)) {
                        byte[] read;
                        try {
                            read = fileService.read(urlEntry.getValue());
                        } catch (IOException e) {
                            logger.error(String.format("无法获取html:[%s]->[%s]", urlEntry.getName(), urlEntry.getValue()));
                            urlEntry.setValue(null);
                            continue;
                        }
                        String html = new String(read);
                        List<UrlEntry> seedScripts = seed.getScripts();
                        //查找需要下载的javascript文件
                        List<UrlRule> scriptRules = urlQuery.getScriptRules();
                        if (scriptRules.size() > 0) {
                            //匹配出所有需要下载的url地址
                            List<String> htmlJsUrls = getJsUrlsFromHtml(html, scriptRules);
                            List<String> needDownLoadJsUrls;
                            if (seedScripts != null)
                                needDownLoadJsUrls = htmlJsUrls.stream()
                                        .filter(htmlJsUrl -> seedScripts.stream().noneMatch(seedScript -> htmlJsUrl.equals(seedScript.getName())))
                                        .collect(Collectors.toList());
                            else
                                needDownLoadJsUrls = htmlJsUrls;
                            //如果有需要下载的js,则添加在seed里，发回downloader下载
                            if (needDownLoadJsUrls.size() > 0) {
                                needDownLoadJsUrls.forEach(url -> seed.addToScripts(new UrlEntry(url)));
                                downLoadSender.send(seed);
                                return;
                            }
                            //否则，执行解析js。解析html
                            if (seedScripts != null) {
                                List<String> scripts = seedScripts.stream().map(entry -> {
                                    try {
                                        return new String(fileService.read(entry.getValue()));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    return "";
                                }).collect(Collectors.toList());
                                html = runJsOnHtml(html, scripts);
                            }

                            //TODO 匹配html
                            Document document = Jsoup.parse(html);
                            Element body = document.body();
                            List<String> savedCssSelectors = urlQuery.getSavedCssSelectors();
                            for (String savedCssSelector : savedCssSelectors) {
                                Elements select = body.select(savedCssSelector);
                                String result = select.html();
                                ContentResult contentResult = new ContentResult(seed.getId());


                            }
                        }
                        //如果有js没有下载完全,返回给downloader继续下载
                        if (seedScripts.stream().anyMatch(entry -> entry.getValue() == null)) {
                            downLoadSender.send(seed);
                        }

                    }
                }
            }
        });
    }

    /**
     * 根据js匹配规则，获取对应的js url
     *
     * @param html        html字符串
     * @param scriptRules js匹配规则集合
     * @return 新的html页面
     */
    private List<String> getJsUrlsFromHtml(String html, List<UrlRule> scriptRules) {
        return new ArrayList<>();
    }

    //TODO 待实现
    private String runJsOnHtml(String html, List<String> scripts) {
        return html;
    }

    /**
     * 检测匹配
     *
     * @param urlQuery url 规则
     * @param urlEntry url入口
     * @return true/false 是否匹配
     */
    private boolean matchRule(UrlQuery urlQuery, UrlEntry urlEntry) {
        List<UrlRule> urlRules = urlQuery.getUrlRules();
        for (UrlRule urlRule : urlRules) {
            Rule rule = urlRule.getRule();
            Ruler ruler = RuleFactory.createRuler(rule);
            ruler.init(urlRule.getValue());
            if (ruler.match(urlEntry.getName())) {
                return true;
            }
        }
        return false;
    }

    @Autowired
    public void setSchedulerRepository(SchedulerRepository schedulerRepository) {
        this.schedulerRepository = schedulerRepository;
    }

    @Autowired
    public void setFileService(IFileService fileService) {
        this.fileService = fileService;
    }

    @Autowired
    public void setDownLoadSender(DownLoadSender downLoadSender) {
        this.downLoadSender = downLoadSender;
    }
}
