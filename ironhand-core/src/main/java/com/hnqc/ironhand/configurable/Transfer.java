package com.hnqc.ironhand.configurable;

import java.util.Map;

/**
 * Transfer
 *
 * @author zido
 * @date 2018/04/12
 */
public interface Transfer<T> {
    /**
     * 对象转map
     *
     * @param t 对象
     * @return map
     */
    Map<String, Object> toMap(T t);


    /**
     * map转对象
     *
     * @param map map
     * @return object
     */
    T toObj(Map<String, Object> map);
}
