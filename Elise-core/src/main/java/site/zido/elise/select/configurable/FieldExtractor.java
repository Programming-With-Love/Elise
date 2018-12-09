package site.zido.elise.select.configurable;

import site.zido.elise.select.Selector;

/**
 * 抽取器描述
 * <p>
 * 配置抽取内容的规则
 * <p>
 * 可做为单独的每条属性配置
 *
 * @author zido
 */
public interface FieldExtractor {
    /**
     * Gets name.
     *
     * @return the name
     */
    String getName();

    /**
     * Gets source.
     *
     * @return the source
     */
    Source getSource();

    /**
     * Gets selector.
     *
     * @return the selector
     */
    Selector getSelector();

    /**
     * Gets nullable.
     *
     * @return the nullable
     */
    boolean getNullable();
}
