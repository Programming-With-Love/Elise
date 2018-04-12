package com.hnqc.ironhand.common.constants;

/**
 * 规则
 */
public enum Rule {
    /**
     * 等于
     */
    EQUALS(1, "等于"),
    /**
     * 描述
     */
    SCRIPT(2, "描述"),
    /**
     * 正则
     */
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
