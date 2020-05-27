package site.zido.elise.task.api;

/**
 * 抽取范围
 *
 * @author zido
 */
public class Source {
    public final static String URL = "url";
    public final static String CODE = "code";
    public final static String BODY = "body";
    public final static String HTML = "html";
    public final static String TEXT = "text";
    public final static String PARTITION = "partition";
    private Source() {
    }

    public static boolean matchSource(String target, String... source) {
        if (source.length == 0) {
            return false;
        }
        if (target == null || "".equals(target)) {
            return false;
        }
        for (String s : source) {
            if (s.equalsIgnoreCase(target)) {
                return true;
            }
        }
        return false;
    }
}
