package site.zido.elise.select.api.impl;

import site.zido.elise.select.Matcher;
import site.zido.elise.select.Selector;
import site.zido.elise.select.api.MatchResult;
import site.zido.elise.select.api.PairResult;
import site.zido.elise.select.api.Text;

public class DefaultPaiResult implements PairResult {
    @Override
    public MatchResult matchKey(Matcher matcher) {
        return null;
    }

    @Override
    public MatchResult matchValue(Matcher matcher) {
        return null;
    }

    @Override
    public Text selectKey(Selector selector) {
        return null;
    }

    @Override
    public Text selectValue(Selector selector) {
        return null;
    }
}
