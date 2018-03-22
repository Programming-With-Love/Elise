package com.hnqc.ironhand.common.pojo;

import com.hnqc.ironhand.common.constants.Rule;
import com.hnqc.ironhand.common.pojo.entity.Scheduler;
import com.hnqc.ironhand.common.query.UrlQuery;
import org.junit.Assert;
import org.junit.Test;

public class SchedulerTest {
    @Test
    public void testBuild() {
        Scheduler scheduler = Scheduler.builder()
                .id(123L)
                .buildQuery()
                .url().add(new UrlRule(Rule.EQUALS, "baidu.com")).toTop()
                .js().add(new UrlRule(Rule.EQUALS, "baidu.com/1.js")).toTop()
                .selector()
                .find()
                .add("findCss1")
                .add("findCss2")
                .toTop()
                .saveAndFind()
                .add("findCss3")
                .add("findCss4")
                .toTop().build();
        Assert.assertEquals(scheduler.getScheduleId().longValue(), 123L);
        Assert.assertEquals(1, scheduler.getUrlQueries().size());
        for (UrlQuery urlQuery : scheduler.getUrlQueries()) {
            Assert.assertEquals(4, urlQuery.getCssSelectors().size());
        }
    }
}
