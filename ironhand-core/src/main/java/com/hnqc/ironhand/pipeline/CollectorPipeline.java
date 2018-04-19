package com.hnqc.ironhand.pipeline;

import java.util.List;

public interface CollectorPipeline<T> extends Pipeline {

    public List<T> getCollection();
}
