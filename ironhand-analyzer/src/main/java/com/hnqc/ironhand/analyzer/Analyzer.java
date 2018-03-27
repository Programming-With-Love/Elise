package com.hnqc.ironhand.analyzer;

import com.hnqc.ironhand.common.constants.Rule;
import com.hnqc.ironhand.common.pojo.UrlEntry;
import com.hnqc.ironhand.common.pojo.UrlRule;
import com.hnqc.ironhand.common.pojo.entity.Scheduler;
import com.hnqc.ironhand.common.pojo.entity.Seed;
import com.hnqc.ironhand.common.query.RuleFactory;
import com.hnqc.ironhand.common.query.Ruler;
import com.hnqc.ironhand.common.query.UrlQuery;
import com.hnqc.ironhand.common.repository.SchedulerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class Analyzer {
    private SchedulerRepository schedulerRepository;

    public void analysis(Seed seed) {
        Long schedulerId = seed.getSchedulerId();
        Optional<Scheduler> optional = schedulerRepository.findById(schedulerId);
        optional.ifPresent(scheduler -> {
            List<UrlQuery> urlQueries = scheduler.getUrlQueries();
            for (UrlQuery urlQuery : urlQueries) {
                for (UrlEntry urlEntry : seed.getUrls()) {
                    if (matchRule(urlQuery, urlEntry)) {
                        List<UrlRule> scriptRules = urlQuery.getScriptRules();

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
}
