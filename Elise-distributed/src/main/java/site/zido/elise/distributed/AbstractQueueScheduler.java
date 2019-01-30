package site.zido.elise.distributed;

import site.zido.elise.scheduler.AbstractScheduler;
import site.zido.elise.scheduler.Seed;

/**
 * 使用消息队列实现的分布式任务调度器基类
 *
 * @author zido
 */
public abstract class AbstractQueueScheduler extends AbstractScheduler implements Runnable {
    @Override
    public void run() {

    }

    protected abstract Seed readSeedFromQueue();
}
