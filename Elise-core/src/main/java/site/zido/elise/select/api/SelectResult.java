package site.zido.elise.select.api;

import site.zido.elise.select.Selector;

/**
 * The interface Select result.
 *
 * @author zido
 */
public interface SelectResult {
    /**
     * As select result.
     *
     * @param fieldName the field name
     * @return the select result
     */
    SelectResult as(String fieldName);

    /**
     * Save text select result.
     *
     * @return the select result
     */
    SelectResult saveText();

    /**
     * Select select result.
     *
     * @param selector the selector
     * @return the select result
     */
    SelectResult select(Selector selector);

    /**
     * Nullable select result.
     *
     * @param nullable the nullable
     * @return the select result
     */
    SelectResult nullable(boolean nullable);
}
