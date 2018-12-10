package site.zido.elise.select.api;

import site.zido.elise.select.LinkSelector;
import site.zido.elise.select.RegexSelector;

/**
 * The interface Match result.
 *
 * @author zido
 */
public interface MatchResult {
    /**
     * Whether the match is the target page
     * <p>
     * only supports regex now.
     *
     * @param selector the regex selector
     * @return match result
     */
    MatchResult asTarget(RegexSelector selector);

    /**
     * select target url from body
     * <p>
     * only supports regex now.
     *
     * @param linkSelector the link selector
     * @return match result
     */
    MatchResult asHelp(LinkSelector linkSelector);
}
