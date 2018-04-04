package com.hnqc.ironhand.spider.distributed;

import com.hnqc.ironhand.spider.Site;
import com.hnqc.ironhand.spider.Task;

/**
 * 可用于分布式传输的task
 * <br>
 * 用作信息载体，仅携带必要信息
 */
public class DistributedTask implements Task {
    private Long ID;
    private Site site;

    public DistributedTask(Task task) {
        this.ID = task.getID();
        this.site = task.getSite();
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }
}
