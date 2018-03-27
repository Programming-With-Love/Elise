package com.hnqc.ironhand.common.constants;

public enum Rule {
    EQUALS(1, "等于"),
    SCRIPT(2, "描述"),
    REGEX(3, "正则");

    private int type;
    private String description;

    Rule(int type, String description) {
        this.type = type;
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
