package site.zido.elise;

import site.zido.elise.scheduler.DefaultTaskScheduler;

public class SpiderBuilder {
    protected SpiderBuilder() {
        super();
    }

    public static SpiderBuilder create() {
        return new SpiderBuilder();
    }

    public static Spider build() {
        return new DefaultTaskScheduler();
    }

    public static Spider defaults() {
        return new DefaultTaskScheduler();
    }
}
