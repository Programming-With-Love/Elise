package com.hnqc.ironhand.common.query;

public interface Ruler {
    /**
     * 初始化方法
     */
    default void init(String origin) {

    }

    boolean match(String target);
}
