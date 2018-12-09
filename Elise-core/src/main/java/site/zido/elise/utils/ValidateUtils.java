package site.zido.elise.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * validate util
 *
 * @author zido
 * @since 2017 /5/25 0025
 */
public class ValidateUtils {

    /**
     * Is empty boolean.
     *
     * @param array the array
     * @return the boolean
     */
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Is empty boolean.
     *
     * @param array the array
     * @return the boolean
     */
    public static boolean isEmpty(long[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Is empty boolean.
     *
     * @param array the array
     * @return the boolean
     */
    public static boolean isEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Is empty boolean.
     *
     * @param array the array
     * @return the boolean
     */
    public static boolean isEmpty(short[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Is empty boolean.
     *
     * @param array the array
     * @return the boolean
     */
    public static boolean isEmpty(char[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Is empty boolean.
     *
     * @param array the array
     * @return the boolean
     */
    public static boolean isEmpty(byte[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Is empty boolean.
     *
     * @param array the array
     * @return the boolean
     */
    public static boolean isEmpty(double[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Is empty boolean.
     *
     * @param array the array
     * @return the boolean
     */
    public static boolean isEmpty(float[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Is empty boolean.
     *
     * @param array the array
     * @return the boolean
     */
    public static boolean isEmpty(boolean[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Is empty boolean.
     *
     * @param list the list
     * @return the boolean
     */
    public static boolean isEmpty(Collection<?> list) {
        return null == list || list.size() == 0;
    }

    /**
     * Is not empty boolean.
     *
     * @param list the list
     * @return the boolean
     */
    public static boolean isNotEmpty(Collection<?> list) {
        return !isEmpty(list);
    }

    /**
     * Is empty boolean.
     *
     * @param s the s
     * @return the boolean
     */
    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    /**
     * Is empty boolean.
     *
     * @param i the
     * @return the boolean
     */
    public static boolean isEmpty(Integer i) {
        return i == null;
    }

    /**
     * Is empty boolean.
     *
     * @param l the l
     * @return the boolean
     */
    public static boolean isEmpty(Long l) {
        return l == null;
    }

    /**
     * Is empty boolean.
     *
     * @param b the b
     * @return the boolean
     */
    public static boolean isEmpty(Boolean b) {
        return b == null;
    }

    /**
     * Is all empty boolean.
     *
     * @param lists the lists
     * @return the boolean
     */
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
     * @return 是 /否
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

    /**
     * Is empty boolean.
     *
     * @param cookies the cookies
     * @return the boolean
     */
    public static boolean isEmpty(Map cookies) {
        return cookies == null || cookies.size() == 0;
    }
}
