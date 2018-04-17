package com.hnqc.ironhand.common.query;

import com.hnqc.ironhand.common.constants.Status;
import com.hnqc.ironhand.common.pojo.UrlRule;
import com.hnqc.ironhand.common.pojo.entity.Scheduler;
import com.hnqc.ironhand.utils.ListBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 任务构造器
 *
 * @author zido
 * @date 2018/34/17
 */
public class SchedulerBuilder {
    private Scheduler scheduler;

    public SchedulerBuilder() {
        this.scheduler = new Scheduler();
        initDefaultTask();
    }

    private SchedulerBuilder(QueryBuilder queryBuilder) {
        this.scheduler = new Scheduler();
        initDefaultTask();
        if (queryBuilder != null && queryBuilder.urlQuery != null) {
            List<UrlQuery> urlQueries = new ArrayList<>();
            urlQueries.add(queryBuilder.urlQuery);
            this.scheduler.setUrlQueries(urlQueries);
        }
    }

    private void initDefaultTask() {
        scheduler.setCreateTime(new Date());
        scheduler.setStatus(Status.RUNNING);
    }

    public SchedulerBuilder id(Long id) {
        scheduler.setScheduleId(id);
        return this;
    }

    public SchedulerBuilder status(Status status) {
        scheduler.setStatus(status);
        return this;
    }

    public SchedulerBuilder createTime(Date createTime) {
        scheduler.setCreateTime(createTime);
        return this;
    }

    public QueryBuilder buildQuery() {
        if (scheduler.getUrlQueries() == null) {
            scheduler.setUrlQueries(new ArrayList<>());
        }
        return new QueryBuilder(this);
    }

    public Scheduler build() {
        return scheduler;
    }

    public class QueryBuilder {
        private UrlQuery urlQuery;
        private SchedulerBuilder builder;

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

            public ListBuilder<String, QueryBuilder.Opt> find() {
                return new ListBuilder<>(findList, this);
            }

            public ListBuilder<String, QueryBuilder.Opt> saveAndFind() {
                return new ListBuilder<String, QueryBuilder.Opt>(saveList, this) {
                    @Override
                    public ListBuilder<String, QueryBuilder.Opt> add(String ele) {
                        findList.add(ele);
                        return super.add(ele);
                    }

                    @Override
                    public ListBuilder<String, QueryBuilder.Opt> remove(String ele) {
                        findList.remove(ele);
                        return super.remove(ele);
                    }
                };
            }

            public QueryBuilder buildQuery() {
                return new QueryBuilder(SchedulerBuilder.this);
            }

            public Scheduler build() {

                return QueryBuilder.this.build();
            }

            public QueryBuilder.Opt newXpath() {
                return QueryBuilder.this.xPath();
            }

            public QueryBuilder.Opt newSelector() {
                return QueryBuilder.this.xPath();
            }
        }

        public QueryBuilder.Opt xPath() {
            if (urlQuery.getSavedXPaths() == null) {
                urlQuery.setSavedXPaths(new ArrayList<>());
            }
            if (urlQuery.getXPaths() == null) {
                urlQuery.setXPaths(new ArrayList<>());
            }
            return new QueryBuilder.Opt(urlQuery.getSavedXPaths(), urlQuery.getXPaths());
        }

        public QueryBuilder.Opt selector() {
            if (urlQuery.getSavedCssSelectors() == null) {
                urlQuery.setSavedCssSelectors(new ArrayList<>());
            }
            if (urlQuery.getCssSelectors() == null) {
                urlQuery.setCssSelectors(new ArrayList<>());
            }
            return new QueryBuilder.Opt(urlQuery.getSavedCssSelectors(), urlQuery.getCssSelectors());
        }

        private QueryBuilder(SchedulerBuilder builder) {
            urlQuery = new UrlQuery();
            this.builder = builder;
        }

        public ListBuilder<UrlRule, QueryBuilder> url() {
            if (urlQuery.getUrlRules() == null) {
                urlQuery.setUrlRules(new ArrayList<>());
            }
            return new ListBuilder<>(urlQuery.getUrlRules(), this);
        }

        public ListBuilder<UrlRule, QueryBuilder> js() {
            if (urlQuery.getScriptRules() == null) {
                urlQuery.setScriptRules(new ArrayList<>());
            }
            return new ListBuilder<>(urlQuery.getScriptRules(), this);
        }

        public SchedulerBuilder reset() {
            SchedulerBuilder builder;
            if (this.builder != null) {
                builder = this.builder;
            } else {
                builder = new SchedulerBuilder(this);
            }
            Scheduler scheduler = builder.scheduler;
            List<UrlQuery> urlQueries = scheduler.getUrlQueries();
            if (urlQueries == null) {
                urlQueries = new ArrayList<>();
                scheduler.setUrlQueries(urlQueries);
            }
            urlQueries.add(urlQuery);
            return builder;
        }

        public Scheduler build() {
            return reset().build();
        }

        public QueryBuilder buildQuery() {
            List<UrlQuery> urlQueries = scheduler.getUrlQueries();
            if (urlQueries == null) {
                urlQueries = new ArrayList<>();
                scheduler.setUrlQueries(urlQueries);
            }
            urlQueries.add(urlQuery);
            return new QueryBuilder(this.builder);
        }
    }
}
