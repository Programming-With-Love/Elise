package site.zido.elise.select.api;

import site.zido.elise.select.Selector;

/**
 * The type Html.
 *
 * @author zido
 */
public class Html implements SelectResult, MatchResult {
    @Override
    public MatchResult asTarget() {
        return null;
    }

    @Override
    public MatchResult asHelp() {
        return null;
    }

    @Override
    public SelectResult as(String fieldName) {
        return null;
    }

    @Override
    public SelectResult text() {
        return null;
    }

    @Override
    public SelectResult richText() {
        return null;
    }

    @Override
    public SelectResult origin() {
        return null;
    }

    @Override
    public SelectResult select(Selector selector) {
        return null;
    }

    @Override
    public SelectResult nullable(boolean nullable) {
        return null;
    }
}
