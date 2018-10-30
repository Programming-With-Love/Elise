package site.zido.elise.select.configurable;

import com.virjar.sipsoup.exception.XpathSyntaxErrorException;
import site.zido.elise.select.CssSelector;
import site.zido.elise.select.RegexSelector;
import site.zido.elise.select.Selector;
import site.zido.elise.select.XPathSelector;

/**
 * 抽取器描述
 * <p>
 * 配置抽取内容的规则
 * <p>
 * 可做为单独的每条属性配置，此时{@link #name} 为字段名 <br>
 *
 * @author zido
 */
public class DefExtractor {
    /**
     * 字段名
     */
    private String name;

    /**
     * 抽取值，默认为xpath跟路径
     */
    private String value = "/html/body";
    /**
     * 抽取值类型，默认为xpath
     */
    private ExpressionType type = ExpressionType.XPATH;
    /**
     * 是否允许空 ,默认允许为空
     */
    private Boolean nullable = true;

    /**
     * 选择抽取范围
     */
    private Source source = Source.REGION;

    public DefExtractor(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public DefExtractor setName(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return value;
    }

    public DefExtractor setValue(String value) {
        this.value = value;
        return this;
    }

    public ExpressionType getType() {
        return type;
    }

    public DefExtractor setType(ExpressionType type) {
        this.type = type;
        return this;
    }

    public Boolean getNullable() {
        return nullable;
    }

    public DefExtractor setNullable(Boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    public Source getSource() {
        return source;
    }

    public DefExtractor setSource(Source source) {
        this.source = source;
        return this;
    }

    public Selector compileSelector() {
        ExpressionType type = getType();
        Selector selector;
        switch (type) {
            case CSS:
                selector = new CssSelector(this.value);
                break;
            case REGEX:
                selector = new RegexSelector(this.value);
                break;
            case XPATH:
            default:
                try {
                    selector = new XPathSelector(this.value);
                } catch (XpathSyntaxErrorException e) {
                    //TODO exception handle
                    throw new RuntimeException(e);
                }
        }
        return selector;
    }

}
