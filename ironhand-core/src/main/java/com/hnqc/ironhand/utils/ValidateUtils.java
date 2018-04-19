package com.hnqc.ironhand.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验工具类
 *
 * @author zido
 * @since 2017/5/25 0025
 */
public class ValidateUtils {

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(long[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(short[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(char[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(byte[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(double[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(float[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(boolean[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(List list) {
        return null == list || list.size() == 0;
    }

    public static boolean isNotEmpty(List list) {
        return !isEmpty(list);
    }

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static boolean isEmpty(Integer i) {
        return i == null;
    }

    public static boolean isEmpty(Long l) {
        return l == null;
    }

    public static boolean isEmpty(Boolean b) {
        return b == null;
    }

    public static boolean isAllEmpty(List... lists) {
        for (List list : lists) {
            if (isNotEmpty(list)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 后缀是否能匹配上
     *
     * @param target        待验证后缀字符串
     * @param allowSuffixes 允许后缀字符串
     * @return 是/否
     */
    public static boolean mapSuffix(String target, String... allowSuffixes) {
        if (isEmpty(target)) {
            return false;
        }
        for (String type : allowSuffixes) {
            if (target.endsWith(type)) {
                return true;
            }
        }
        return false;
    }
}
