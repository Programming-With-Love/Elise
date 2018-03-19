package com.hnqc.ironhand.common.pojo;

import com.hnqc.ironhand.common.pojo.entity.Task;
import org.junit.Assert;
import org.junit.Test;

public class TaskTest {
    @Test
    public void testBuild() {
        Task task = Task.builder()
                .id(123L)
                .buildQuery()
                .url(new StringUrlMatcher("baidu.com/1"))
                .js(new StringJsMatcher("baidu.com/1.js"))
                .selector()
                .find()
                .add("findCss1")
                .add("findCss2")
                .toTop()
                .saveAndFind()
                .add("findCss3")
                .add("findCss4")
                .toTop().build();
        Assert.assertEquals(task.getScheduleId().longValue(), 123L);
        Assert.assertEquals(1, task.getUrlQueries().size());
        for (Task.UrlQuery urlQuery : task.getUrlQueries()) {
            Assert.assertEquals(4, urlQuery.getCssSelectors().size());
        }
    }
}
