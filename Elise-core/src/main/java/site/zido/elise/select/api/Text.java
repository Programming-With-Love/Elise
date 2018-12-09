package site.zido.elise.select.api;

import site.zido.elise.select.Selector;

/**
 * The type selectable url
 *
 * @author zido
 */
public interface Text extends Selectable, Matchable, SelectResult, MatchResult {
    Text saveText();

    Text region(Selector selector);
}
