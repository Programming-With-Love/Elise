package com.hnqc.ironhand.schedule.pojo;

import com.hnqc.ironhand.common.constants.Status;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Task {
    /**
     * id
     */
    private Long scheduleId;
    /**
     * 解析规则
     */
    @Transient
    private List<UrlQuery> urlQueries;
    /**
     * 执行次数,-1表示无限次执行
     */
    private Integer times;

    /**
     * 剩余执行次数,-1表示无限次
     */
    private Integer remain;

    /**
     * 当前状态
     */
    private Status status;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    /**
     * 下一次执行时间,所有次数结束设置为null
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date nextTime;

//    private List<String>

    public interface UrlMatcher {
        boolean match(int depth);
    }

    public interface JsMatcher {
        boolean match(String url);
    }

    public interface ResultMatcher {
        String result(String html, int depth);
    }

    public class UrlQuery {
        private UrlMatcher urlMatcher;
        private JsMatcher jsMatcher;
        private List<String> cssSelectors;
        private List<String> xPaths;
        private ResultMatcher resultMatcher;

        public UrlMatcher getUrlMatcher() {
            return urlMatcher;
        }

        public void setUrlMatcher(UrlMatcher urlMatcher) {
            this.urlMatcher = urlMatcher;
        }

        public JsMatcher getJsMatcher() {
            return jsMatcher;
        }

        public void setJsMatcher(JsMatcher jsMatcher) {
            this.jsMatcher = jsMatcher;
        }

        public List<String> getCssSelectors() {
            return cssSelectors;
        }

        public void setCssSelectors(List<String> cssSelectors) {
            this.cssSelectors = cssSelectors;
        }

        public List<String> getXPaths() {
            return xPaths;
        }

        public void setXPaths(List<String> xPaths) {
            this.xPaths = xPaths;
        }

        public ResultMatcher getResultMatcher() {
            return resultMatcher;
        }

        public void setResultMatcher(ResultMatcher resultMatcher) {
            this.resultMatcher = resultMatcher;
        }
    }

    public static class TaskBuilder {
        private Task task;

        private TaskBuilder() {

        }

        private TaskBuilder(QueryBuilder queryBuilder) {
            this.task = new Task();
            if (queryBuilder != null && queryBuilder.urlQuery != null) {
                List<UrlQuery> urlQueries = new ArrayList<>();
                urlQueries.add(queryBuilder.urlQuery);
                this.task.setUrlQueries(urlQueries);
            }
        }

        public TaskBuilder create() {
            this.task = new Task();
            return this;
        }

        public TaskBuilder id(Long id) {
            task.scheduleId = id;
            return this;
        }

        public QueryBuilder buildQuery() {
            if (task.getUrlQueries() == null) {
                task.setUrlQueries(new ArrayList<>());
            }
            return new QueryBuilder(this);
        }

        public Task build() {
            return task;
        }

        public static class QueryBuilder {
            private UrlQuery urlQuery;
            private TaskBuilder builder;

            private QueryBuilder(TaskBuilder builder) {
                this.builder = builder;
            }

            public QueryBuilder url(UrlMatcher urlMatcher) {
                urlQuery.setUrlMatcher(urlMatcher);
                return this;
            }

            public QueryBuilder js(JsMatcher jsMatcher) {
                urlQuery.setJsMatcher(jsMatcher);
                return this;
            }

            public QueryBuilder addCssSelector(String cssSelector) {
                if (urlQuery.getCssSelectors() == null) {
                    urlQuery.setCssSelectors(new ArrayList<>());
                }
                urlQuery.getCssSelectors().add(cssSelector);
                return this;
            }

            public QueryBuilder addXPath(String xPath) {
                if (urlQuery.getXPaths() == null) {
                    urlQuery.setXPaths(new ArrayList<>());
                }
                urlQuery.getXPaths().add(xPath);
                return this;
            }

            public QueryBuilder result(ResultMatcher resultMatcher) {
                urlQuery.setResultMatcher(resultMatcher);
                return this;
            }

            public TaskBuilder build() {
                if (this.builder != null) {
                    return this.builder;
                }
                return new TaskBuilder(this);
            }
        }
    }

    public static TaskBuilder builder() {
        return new TaskBuilder();
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public List<UrlQuery> getUrlQueries() {
        return urlQueries;
    }

    public void setUrlQueries(List<UrlQuery> urlQueries) {
        this.urlQueries = urlQueries;
    }
}
