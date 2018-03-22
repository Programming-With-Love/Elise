package com.hnqc.ironhand.common.pojo;

import com.alibaba.fastjson.JSON;
import com.hnqc.ironhand.common.constants.Rule;
import com.hnqc.ironhand.common.pojo.entity.Scheduler;
import com.hnqc.ironhand.common.pojo.entity.Seed;
import com.hnqc.ironhand.common.utils.IdWorker;
import org.junit.Assert;
import org.junit.Test;

public class JSONTest {

    @Test
    public void testJson() {
        Seed seed = new Seed();
        seed.addToUrls(new UrlEntry("www.baidu.com"));
        seed.addToScripts(new UrlEntry("www.baidu.com/1.js"));
        String s = JSON.toJSONString(seed);
        Seed seed1 = JSON.parseObject(s, Seed.class);
        Assert.assertEquals(seed.getUrls().size(), seed1.getUrls().size());

    }

    @Test
    public void testSchedulerJson() {
        Scheduler scheduler = Scheduler.builder()
                .id(IdWorker.nextId())
                .buildQuery()
                .url()
                .add(new UrlRule(Rule.EQUALS, "www.baidu.com"))
                .add(new UrlRule(Rule.EQUALS, "www.baidu.com/1"))
                .toTop()
                .js()
                .add(new UrlRule(Rule.EQUALS, "www.baidu.com/1.js"))
                .toTop()
                .selector()
                .saveAndFind()
                .add("a[class='header']")
                .toTop()
                .build();
        String s = JSON.toJSONString(scheduler);
        System.out.println(s);

        Scheduler result = JSON.parseObject(s, Scheduler.class);

        Assert.assertEquals(result.getScheduleId(), scheduler.getScheduleId());
        Assert.assertTrue(result.getUrlQueries().size() == 1);
    }
}
