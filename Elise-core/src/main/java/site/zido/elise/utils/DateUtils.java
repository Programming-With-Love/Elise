package site.zido.elise.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间日期工具类
 *
 * @author zido
 * @date 2018 /04/25
 * @since 2017 /6/6 0006
 */
public class DateUtils {

    /**
     * 毫秒
     */
    public static final long MS = 1;
    /**
     * 每秒钟的毫秒数
     */
    public static final long SECOND_MS = MS * 1000;
    /**
     * 每分钟的毫秒数
     */
    public static final long MINUTE_MS = SECOND_MS * 60;
    /**
     * 每小时的毫秒数
     */
    public static final long HOUR_MS = MINUTE_MS * 60;
    /**
     * 每天的毫秒数
     */
    public static final long DAY_MS = HOUR_MS * 24;
    /**
     * 每月的毫秒数
     */
    public static final long MONTH_MS = DAY_MS * 30;
    /**
     * 每年的毫秒数
     */
    public static final long YEAR_MS = MONTH_MS * 12;

    /**
     * 标准日期（不含时间）格式化器
     */
    private static final SimpleDateFormat NORM_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 汉化日期格式化器
     */
    private static final SimpleDateFormat CHINESE_DATE_FORMAT = new SimpleDateFormat("yyyy年MM月dd日");

    /**
     * 汉化日期格式化器
     */
    private static final SimpleDateFormat CHINESE_DATETIME_FORMAT = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
    /**
     * 标准日期时间格式化器
     */
    private static final SimpleDateFormat NORM_DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * HTTP日期时间格式化器
     */
    private static final SimpleDateFormat HTTP_DATETIME_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",
            Locale.US);

    /**
     * The constant TYPE_YEAR.
     */
    public static final int TYPE_YEAR = 0;

    /**
     * The constant TYPE_MOUTH.
     */
    public static final int TYPE_MOUTH = 1;

    /**
     * The constant TYPE_DAY.
     */
    public static final int TYPE_DAY = 2;


    /**
     * Instantiates a new Date utils.
     */
    private DateUtils() {

    }

    /**
     * 当前时间，格式 yyyy-MM-dd HH:mm:ss
     *
     * @return 当前时间的标准形式字符串 string
     */
    public static String now() {
        return formatDateTime(new Date());
    }

    /**
     * Now string.
     *
     * @param formatStr the format str
     * @return the string
     */
    public static String now(String formatStr) {
        return format(new Date(), formatStr);
    }

    /**
     * 格式 yyyy-MM-dd HH:mm:ss
     *
     * @param date 被格式化的日期
     * @return 格式化后的日期 string
     */
    public static String formatDateTime(Date date) {
        return NORM_DATETIME_FORMAT.format(date);
    }

    /**
     * 当前日期，格式 yyyy-MM-dd
     *
     * @return 当前日期的标准形式字符串 string
     */
    public static String today() {
        return formatDate(new Date());
    }

    /**
     * 根据特定格式格式化日期
     *
     * @param date   被格式化的日期
     * @param format 格式
     * @return 格式化后的字符串 string
     */
    public static String format(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 格式化为Http的标准日期格式
     *
     * @param date 被格式化的日期
     * @return HTTP标准形式日期字符串 string
     */
    public static String formatHttpDate(Date date) {
        return HTTP_DATETIME_FORMAT.format(date);
    }

    /**
     * 格式 yyyy-MM-dd
     *
     * @param date 被格式化的日期
     * @return 格式化后的字符串 string
     */
    public static String formatDate(Date date) {
        return NORM_DATE_FORMAT.format(date);
    }

    /**
     * 将特定格式的日期转换为Date对象
     *
     * @param dateString 特定格式的日期
     * @param format     格式，例如yyyy-MM-dd
     * @return 日期对象 date
     */
    public static Date parse(String dateString, String format) {
        try {
            return (new SimpleDateFormat(format)).parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 此方法会尽可能根据你提供的字符串解析出正确的时间
     *
     * @param dateString the date string
     * @param type       the type
     * @return date date
     */
    public static Date parse(String dateString, int type) {
        String[] format;
        switch (type) {
            case TYPE_YEAR:
                format = new String[]{"yyyy", "yyyy年"};
                break;
            case TYPE_MOUTH:
                format = new String[]{"yyyy年MM月", "MM月yyyy年", "yyyy-MM", "yyyy/MM", "yyyy:MM"};
                break;
            case TYPE_DAY:
            default:
                format = new String[]{"yyyy年MM月dd日", "MM月dd日yyyy年", "yyyy-MM-dd", "yyyy/MM/dd", "yyyy:MM:dd"};
        }
        for (String s : format) {
            try {
                Date parse = (new SimpleDateFormat(s)).parse(dateString);
                if (parse != null) {
                    return parse;
                }
            } catch (ParseException ignore) {
            }

        }
        return null;
    }

    /**
     * 格式yyyy-MM-dd HH:mm:ss
     *
     * @param dateString 标准形式的时间字符串
     * @return 日期对象 date
     */
    public static Date parseDateTime(String dateString) {
        try {
            return NORM_DATETIME_FORMAT.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 格式yyyy-MM-dd
     *
     * @param dateString 标准形式的日期字符串
     * @return 标准形式的日期字符串 date
     */
    public static Date parseDate(String dateString) {
        try {
            return NORM_DATE_FORMAT.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定日期偏移指定时间后的时间
     *
     * @param date          基准日期
     * @param calendarField 偏移的粒度大小（小时、天、月等）使用Calendar中的常数
     * @param offsite       偏移量，正数为向后偏移，负数为向前偏移
     * @return 偏移后的日期 offsite date
     */
    public static Date getOffsiteDate(Date date, int calendarField, int offsite) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(calendarField, offsite);
        return cal.getTime();
    }

    /**
     * 判断两个日期相差的时长<br/>(列：1年前7月25日)
     * <p>
     * 返回 minuend - subtrahend 的差
     *
     * @param subtrahend 减数日期
     * @param minuend    被减数日期
     * @return 日期差 string
     */
    public static String dateDiff(Date subtrahend, Date minuend) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(subtrahend);
        long diff = minuend.getTime() - subtrahend.getTime();
        if (diff <= HOUR_MS) {
            return diff / MINUTE_MS + "分钟前";
        } else if (diff <= DAY_MS) {
            return diff / HOUR_MS + "小时" + (diff % HOUR_MS / MINUTE_MS) + "分钟前";
        } else if (diff <= DAY_MS * 2) {
            return "昨天" + calendar.get(Calendar.HOUR_OF_DAY) + "点" + calendar.get(Calendar.MINUTE) + "分";
        } else if (diff <= DAY_MS * 3) {
            return "前天" + calendar.get(Calendar.HOUR_OF_DAY) + "点" + calendar.get(Calendar.MINUTE) + "分";
        } else if (diff <= MONTH_MS) {
            return diff / DAY_MS + "天前" + calendar.get(Calendar.HOUR_OF_DAY) + "点" + calendar.get(Calendar.MINUTE) + "分";
        } else if (diff <= YEAR_MS) {
            return diff / MONTH_MS + "个月" + (diff % MONTH_MS) / DAY_MS + "天前" + calendar.get(Calendar.HOUR_OF_DAY) + "点" + calendar.get(Calendar.MINUTE) + "分";
        } else {
            return diff / YEAR_MS + "年前" + (calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DATE) + "日";
        }
    }


    /**
     * 距离截止日期还有多长时间
     *
     * @param date the date
     * @return string string
     */
    public static String fromDeadline(Date date) {
        long deadline = date.getTime();
        long now = System.currentTimeMillis();
        long remain = deadline - now;
        if (remain <= HOUR_MS) {
            return "只剩下" + remain / MINUTE_MS + "分钟";
        } else if (remain <= DAY_MS) {
            return "只剩下" + remain / HOUR_MS + "小时"
                    + (remain % HOUR_MS / MINUTE_MS) + "分钟";
        } else {
            long day = remain / DAY_MS;
            long hour = remain % DAY_MS / HOUR_MS;
            long minute = remain % DAY_MS % HOUR_MS / MINUTE_MS;
            return "只剩下" + day + "天" + hour + "小时" + minute + "分钟";
        }
    }

    /**
     * After date boolean.
     *
     * @param beforeTime the before time
     * @param afterTime  the after time
     * @param time       the time
     * @return the boolean
     */
    public static boolean afterDate(Date beforeTime, Date afterTime, long time) {
        return afterTime.getTime() - beforeTime.getTime() > time;
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        Date date = DateUtils.parse("2015", TYPE_YEAR);
        System.out.println(date);
    }
}