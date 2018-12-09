package site.zido.elise.select.api.impl;

import site.zido.elise.http.Header;
import site.zido.elise.select.Matcher;
import site.zido.elise.select.Selector;
import site.zido.elise.select.api.MatchResult;
import site.zido.elise.select.api.PairResult;
import site.zido.elise.select.api.SelectableHeader;

import java.util.ArrayList;

public class DefaultSelectableHeader extends ArrayList<Header> implements SelectableHeader {
    @Override
    public PairResult key(Selector selector) {
        return null;
    }

    @Override
    public MatchResult key(Matcher matcher) {
        return null;
    }

    @Override
    public PairResult value(Selector selector) {
        return null;
    }

    @Override
    public MatchResult value(Matcher matcher) {
        return null;
    }
}
