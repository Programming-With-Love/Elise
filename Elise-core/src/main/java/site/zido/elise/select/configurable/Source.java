package site.zido.elise.select.configurable;

/**
 * 抽取范围
 *
 * @author zido
 */
public enum Source {
    /**
     * 从已选择的html内容中抽取
     */
    REGION,
    /**
     * 从原html中抽取
     */
    RAW_HTML,
    /**
     * 从url中抽取
     */
    URL,
    /**
     * 从文本中抽取
     */
    RAW_TEXT,
    /**
     * from status code
     */
    CODE;

    public static final String REGION_ = "region";
    public static final String HTML_ = "html";
    public static final String URL_ = "url";
    public static final String TEXT_ = "text";
    public static final String CODE_ = "code";
}
