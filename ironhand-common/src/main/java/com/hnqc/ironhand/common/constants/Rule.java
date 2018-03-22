package com.hnqc.ironhand.common.constants;

public enum  Rule {
    EQUALS(1, "等于"),
    SCRIPT(2, "描述");

    private int type;
    private String desciption;

    Rule(int type, String description) {
        this.type = type;
        this.desciption = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }
}
