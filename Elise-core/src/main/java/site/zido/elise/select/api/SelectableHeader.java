package site.zido.elise.select.api;

import site.zido.elise.select.Matcher;
import site.zido.elise.select.Selector;

public interface SelectableHeader {
    PairResult key(Selector selector);

    MatchResult key(Matcher matcher);

    PairResult value(Selector selector);

    MatchResult value(Matcher matcher);
}
