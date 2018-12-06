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
    String getName();

    Source getSource();

    Selector getSelector();

    boolean getNullable();
}
