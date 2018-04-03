package com.hnqc.ironhand.spider;

import com.hnqc.ironhand.spider.processor.PageProcessor;

public class DistributedSpider extends Spider {
    /**
     * create a spider with pageProcessor.
     *
     * @param pageProcessor pageProcessor
     */
    public DistributedSpider(PageProcessor pageProcessor) {
        super(pageProcessor);
    }

    @Override
    protected void initComponent() {
        super.initComponent();
    }
}
