package com.hnqc.ironhand.spider.distributed;

import com.hnqc.ironhand.spider.Site;
import com.hnqc.ironhand.spider.Task;

/**
 * 可用于分布式传输的task
 * <br>
 * 用作信息载体，仅携带必要信息
 *
 * @author zido
 * @date 2018/04/16
 */
public class DistributedTask implements Task {
    private Long id;
    private Site site;

    public DistributedTask(Task task) {
        this.id = task.getId();
        this.site = task.getSite();
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }
}
