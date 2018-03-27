package com.hnqc.ironhand.common.query;

public class EqualsRuler implements Ruler {
    private String origin;

    @Override
    public void init(String origin) {
        this.origin = origin;
    }

    @Override
    public boolean match(String target) {
        return origin != null && origin.equals(target);
    }
}
