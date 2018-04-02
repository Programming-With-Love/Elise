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
      if (isNotEmpty(list))
        return false;
    }
    return true;
  }

  /**
   * 验证邮箱
   *
   * @param str 待验证的字符串
   * @return 如果是符合的字符串, 返回 <b>true </b>,否则为 <b>false </b>
   */
  public static boolean isEmail(String str) {
    String regex = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    return match(regex, str);
  }

  /**
   * 验证身份证
   *
   * @param str 字符串
   * @return
   */
  public static boolean isUserIdentity(String str) {
    String regex = "^((1[1-5])|(2[1-3])|(3[1-7])|(4[1-6])|(5[0-4])|(6[1-5])|71|(8[12])|91)\\d{4}(((((19|20)((\\d{2}(0[13-9]|1[012])(0[1-9]|[12]\\d|30))|(\\d{2}(0[13578]|1[02])31)|(\\d{2}02(0[1-9]|1\\d|2[0-8]))|(([13579][26]|[2468][048]|0[48])0229)))|20000229)\\d{3}(\\d|X|x))|(((\\d{2}(0[13-9]|1[012])(0[1-9]|[12]\\d|30))|(\\d{2}(0[13578]|1[02])31)|(\\d{2}02(0[1-9]|1\\d|2[0-8]))|(([13579][26]|[2468][048]|0[48])0229))\\d{3}))$";
    return match(regex, str);
  }

  /**
   * 验证电话号码
   *
   * @param str 待验证的字符串
   * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
   */
  public static boolean isPhone(String str) {
    String regex = "^[1][3,4,5,7,8][0-9]{9}$";
    return match(regex, str);
  }

  /**
   * 验证输入密码条件(字符与数据同时出现)
   *
   * @param str 待验证的字符串
   * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
   */
  public static boolean isPassword(String str) {
    String regex = "((?=.*\\d)(?=.*\\D)|(?=.*[a-zA-Z])(?=.*[^a-zA-Z]))^.{6,20}$";
    return match(regex, str);
  }

  /**
   * 验证IP地址
   *
   * @param str 待验证的字符串
   * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
   */
  public static boolean isIP(String str) {
    String num = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";
    String regex = "^" + num + "\\." + num + "\\." + num + "\\." + num + "$";
    return match(regex, str);
  }

  /**
   * 验证网址Url
   *
   * @param str 待验证的字符串
   * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
   */
  public static boolean isUrl(String str) {
    String regex = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
    return match(regex, str);
  }

  /**
   * 验证输入密码长度 (6-18位)
   *
   * @param str 待验证的字符串
   * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
   */
  public static boolean isPasswLength(String str) {
    String regex = "[0-9a-zA-Z]{6,16}";
    return match(regex, str);
  }

  /**
   * 验证输入邮政编号
   *
   * @param str 待验证的字符串
   * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
   */
  public static boolean isPostalcode(String str) {
    String regex = "^\\d{6}$";
    return match(regex, str);
  }

  /**
   * 验证输入手机号码
   *
   * @param str 待验证的字符串
   * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
   */
  public static boolean isHandset(String str) {
    String regex = "^[1]+[3,5]+\\d{9}$";
    return match(regex, str);
  }

  /**
   * 验证输入两位小数
   *
   * @param str 待验证的字符串
   * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
   */
  public static boolean isDecimal(String str) {
    String regex = "^[0-9]+(.[0-9]{2})?$";
    return match(regex, str);
  }

  /**
   * 验证输入一年的12个月
   *
   * @param str 待验证的字符串
   * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
   */
  public static boolean isMonth(String str) {
    String regex = "^(0?[[1-9]|1[0-2])$";
    return match(regex, str);
  }

  /**
   * 验证输入一个月的31天
   *
   * @param str 待验证的字符串
   * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
   */
  public static boolean isDay(String str) {
    String regex = "^((0?[1-9])|((1|2)[0-9])|30|31)$";
    return match(regex, str);
  }

  /**
   * 验证日期时间
   *
   * @param str 待验证的字符串
   * @return 如果是符合网址格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
   */
  public static boolean isDate(String str) {
    String regex = "^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-)) (20|21|22|23|[0-1]?\\d):[0-5]?\\d:[0-5]?\\d$";
    return match(regex, str);
  }

  /**
   * 验证数字输入
   *
   * @param str 待验证的字符串
   * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
   */
  public static boolean isNumber(String str) {
    String regex = "^[0-9]*$";
    return match(regex, str);
  }

  /**
   * 验证非零的正整数
   *
   * @param str 待验证的字符串
   * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
   */
  public static boolean isIntNumber(String str) {
    String regex = "^\\+?[1-9][0-9]*$";
    return match(regex, str);
  }

  /**
   * 验证大写字母
   *
   * @param str 待验证的字符串
   * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
   */
  public static boolean isUpChar(String str) {
    String regex = "^[A-Z]+$";
    return match(regex, str);
  }

  /**
   * 验证小写字母
   *
   * @param str 待验证的字符串
   * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
   */
  public static boolean isLowChar(String str) {
    String regex = "^[a-z]+$";
    return match(regex, str);
  }

  /**
   * 验证输入字母
   *
   * @param str 待验证的字符串
   * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
   */
  public static boolean isLetter(String str) {
    String regex = "^[A-Za-z]+$";
    return match(regex, str);
  }

  /**
   * 验证输入汉字
   *
   * @param str 待验证的字符串
   * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
   */
  public static boolean isChinese(String str) {
    String regex = "^[\u4e00-\u9fa5],{0,}$";
    return match(regex, str);
  }

  /**
   * 验证输入字符串
   *
   * @param str 待验证的字符串
   * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
   */
  public static boolean isLength(String str) {
    String regex = "^.{8,}$";
    return match(regex, str);
  }

  /**
   * @param regex 正则表达式字符串
   * @param str   要匹配的字符串
   * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
   */
  private static boolean match(String regex, String str) {
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(str);
    return matcher.matches();
  }

  /**
   * 后缀是否能匹配上
   *
   * @param contentType 待验证后缀字符串
   * @param allowTypes  允许后缀字符串
   * @return 是/否
   */
  public static boolean mapPrefix(String contentType, String... allowTypes) {
    if (null == contentType || "".equals(contentType)) {
      return false;
    }
    for (String type : allowTypes) {
      if (contentType.endsWith(type)) {
        return true;
      }
    }
    return false;
  }

  public static boolean isPicture(String filename) {
    String[] pictruePrefixs = new String[]{".jpg", ".png", ".gif", ".bmp"};
    return mapPrefix(filename, pictruePrefixs);
  }

}
