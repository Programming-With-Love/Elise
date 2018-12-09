package site.zido.elise.select.api;

import site.zido.elise.select.Matcher;
import site.zido.elise.select.Selector;

public interface PairResult {
    MatchResult matchKey(Matcher matcher);

    MatchResult matchValue(Matcher matcher);

    SelectResult selectKey(Selector selector);

    SelectResult selectValue(Selector selector);
}
