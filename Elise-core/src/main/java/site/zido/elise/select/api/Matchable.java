package site.zido.elise.select.api;

import site.zido.elise.select.Matcher;

/**
 * The interface Matchable.
 *
 * @author zido
 */
public interface Matchable {
    /**
     * Match .
     *
     * @param matcher the selector
     */
    MatchResult matches(Matcher matcher);
}
