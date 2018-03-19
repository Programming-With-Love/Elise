package com.hnqc.ironhand.common.utils;

import java.util.List;

public class ListBuilder<T, K> {
    private List<T> list;
    private K result;

    public ListBuilder(List<T> list, K result) {
        if (list == null) {
            throw new IllegalArgumentException("构建时集合不能为Null");
        }
        this.list = list;
        this.result = result;
    }

    public ListBuilder<T, K> add(T ele) {
        list.add(ele);
        return this;
    }

    public ListBuilder<T, K> remove(T ele) {
        list.remove(ele);
        return this;
    }

    public K toTop() {
        return result;
    }
}