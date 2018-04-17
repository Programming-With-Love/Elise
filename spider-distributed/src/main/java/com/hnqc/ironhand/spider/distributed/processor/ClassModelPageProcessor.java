package com.hnqc.ironhand.spider.distributed.processor;

import com.hnqc.ironhand.spider.Site;
import com.hnqc.ironhand.spider.distributed.configurable.PageModelExtractor;

import java.util.List;

/**
 * 扩展Model处理器，可进行map到类的转换
 *
 * @author zido
 * @date 2018/04/16
 */
public class ClassModelPageProcessor<T> extends MappedModelPageProcessor {
    private Class<T> classzz;

    public ClassModelPageProcessor(Class<T> classzz, Site site, PageModelExtractor extractor) {
        super(site, extractor);
        this.classzz = classzz;
    }

    @Override
    protected Object transfer(Object obj) {
        //TODO 扩展从obj转换到class
        return super.transfer(obj);
    }
}
