package site.zido.elise.select.api;

import site.zido.elise.select.LinkSelector;
import site.zido.elise.select.RegexSelector;

public interface MatchResult {
    /**
     * Whether the match is the target page
     * <p>
     *
     * @param selector the regex selector
     * @return match result
     * @apiNote only supports regex now.
     */
    MatchResult asTarget(RegexSelector selector);

    /**
     * select target url from body
     * <p>
     *
     * @param linkSelector the link selector
     * @return match result
     * @apiNote only supports regex now.
     */
    MatchResult asHelp(LinkSelector linkSelector);
}
