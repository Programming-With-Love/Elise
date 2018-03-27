package com.hnqc.ironhand.common.query;

public class LikeRuler implements Ruler {
    private String origin;

    @Override
    public void init(String origin) {
        this.origin = origin;
    }

    @Override
    public boolean match(String target) {
        return false;
    }
}
