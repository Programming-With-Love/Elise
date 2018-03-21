package com.hnqc.ironhand.common.pojo.entity;

import com.hnqc.ironhand.common.constants.Status;
import com.hnqc.ironhand.common.utils.ListBuilder;

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
     * 当前状态
     */
    private Status status;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;



    public interface UrlMatcher {
        boolean match(String newUrl, int depth);
    }

    public interface JsMatcher {
        boolean match(String url);
    }

    public interface ResultMatcher {
        String result(String html, int depth);

        //默认不继续查找
        default boolean find() {
            return false;
        }

        //默认存储
        default boolean save() {
            return true;
        }
    }

    public static class UrlQuery {
        private UrlMatcher urlMatcher;
        private JsMatcher jsMatcher;
        private List<String> cssSelectors;
        private List<String> xPaths;
        private List<String> savedCssSelectors;
        private List<String> savedXPaths;
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

        public List<String> getSavedCssSelectors() {
            return savedCssSelectors;
        }

        public void setSavedCssSelectors(List<String> savedCssSelectors) {
            this.savedCssSelectors = savedCssSelectors;
        }

        public List<String> getSavedXPaths() {
            return savedXPaths;
        }

        public void setSavedXPaths(List<String> savedXPaths) {
            this.savedXPaths = savedXPaths;
        }
    }

    public static class TaskBuilder {
        private Task task;

        private TaskBuilder() {
            this.task = new Task();
            initDefaultTask();
        }

        private TaskBuilder(QueryBuilder queryBuilder) {
            this.task = new Task();
            initDefaultTask();
            if (queryBuilder != null && queryBuilder.urlQuery != null) {
                List<UrlQuery> urlQueries = new ArrayList<>();
                urlQueries.add(queryBuilder.urlQuery);
                this.task.setUrlQueries(urlQueries);
            }
        }

        private void initDefaultTask() {
            task.setCreateTime(new Date());
            task.setStatus(Status.RUNNING);
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

        public class QueryBuilder {
            private UrlQuery urlQuery;
            private TaskBuilder builder;

            public class Opt {
                private final List<String> saveList;
                private final List<String> findList;

                private Opt(List<String> saveList, List<String> findList) {
                    if (saveList == null || findList == null) {
                        throw new IllegalArgumentException("构建时集合不能为Null");
                    }
                    this.saveList = saveList;
                    this.findList = findList;
                }

                public ListBuilder<String, Opt> save() {
                    return new ListBuilder<>(saveList, this);
                }

                public ListBuilder<String, Opt> find() {
                    return new ListBuilder<>(findList, this);
                }

                public ListBuilder<String, Opt> saveAndFind() {
                    return new ListBuilder<String, Opt>(saveList, this) {
                        @Override
                        public ListBuilder<String, Opt> add(String ele) {
                            findList.add(ele);
                            return super.add(ele);
                        }

                        @Override
                        public ListBuilder<String, Opt> remove(String ele) {
                            findList.remove(ele);
                            return super.remove(ele);
                        }
                    };
                }

                public QueryBuilder buildQuery() {
                    return new QueryBuilder(TaskBuilder.this);
                }

                public Task build() {

                    return QueryBuilder.this.build();
                }

                public Opt newXpath() {
                    return QueryBuilder.this.xPath();
                }

                public Opt newSelector() {
                    return QueryBuilder.this.xPath();
                }
            }

            public Opt xPath() {
                if (urlQuery.getSavedXPaths() == null) {
                    urlQuery.setSavedXPaths(new ArrayList<>());
                }
                if (urlQuery.getXPaths() == null) {
                    urlQuery.setXPaths(new ArrayList<>());
                }
                return new Opt(urlQuery.getSavedXPaths(), urlQuery.getXPaths());
            }

            public Opt selector() {
                if (urlQuery.getSavedCssSelectors() == null) {
                    urlQuery.setSavedCssSelectors(new ArrayList<>());
                }
                if (urlQuery.getCssSelectors() == null) {
                    urlQuery.setCssSelectors(new ArrayList<>());
                }
                return new Opt(urlQuery.getSavedCssSelectors(), urlQuery.getCssSelectors());
            }

            private QueryBuilder(TaskBuilder builder) {
                urlQuery = new UrlQuery();
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

            public QueryBuilder result(ResultMatcher resultMatcher) {
                urlQuery.setResultMatcher(resultMatcher);
                return this;
            }

            public TaskBuilder reset() {
                TaskBuilder builder;
                if (this.builder != null) {
                    builder = this.builder;
                } else
                    builder = new TaskBuilder(this);
                Task task = builder.task;
                List<UrlQuery> urlQueries = task.getUrlQueries();
                if (urlQueries == null) {
                    urlQueries = new ArrayList<>();
                    task.setUrlQueries(urlQueries);
                }
                urlQueries.add(urlQuery);
                return builder;
            }

            public Task build() {
                return reset().build();
            }

            public QueryBuilder buildQuery() {
                List<UrlQuery> urlQueries = task.getUrlQueries();
                if (urlQueries == null) {
                    urlQueries = new ArrayList<>();
                    task.setUrlQueries(urlQueries);
                }
                urlQueries.add(urlQuery);
                return new QueryBuilder(this.builder);
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
