package site.zido.elise.select.api;

import site.zido.elise.select.Selector;

public interface SelectResult {
    SelectResult as(String fieldName);

    SelectResult saveText();

    SelectResult select(Selector selector);

    SelectResult nullable(boolean nullable);
}
