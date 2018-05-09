package site.zido.elise.utils;

/**
 * 包装异常处理类，框架内部通用异常处理工具类
 *
 * @author zido
 */
public class NestedRuntimeUtils {
    /**
     * 构建异常信息
     *
     * @param msg   异常信息
     * @param cause 异常原因
     * @return 全异常信息
     */
    public static String buildMessage(String msg, Throwable cause) {
        if (cause == null) {
            return msg;
        }
        String result = "";
        if (msg != null) {
            result += "发生异常：" + msg + "; 原因：";
        }
        result += cause;
        return result;
    }

    /**
     * 获取根异常 （溯源时不包含本身）
     *
     * @param original 源异常
     * @return 最顶层的异常/null
     */
    public static Throwable getRootCause(Throwable original) {
        if (original == null) {
            return null;
        }
        Throwable rootCause = null;
        Throwable cause = original.getCause();
        while (cause != null && cause != rootCause) {
            rootCause = cause;
            cause = cause.getCause();
        }
        return cause;
    }

    /**
     * 获取根异常（溯源时包含本身）
     *
     * @param original 源异常
     * @return
     */
    public static Throwable getOriginalCause(Throwable original) {
        Throwable rootCause = getRootCause(original);
        return rootCause == null ? original : rootCause;
    }
}
