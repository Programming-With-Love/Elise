package site.zido.elise.http.impl;

import site.zido.elise.http.Header;

public class DefaultHeader implements Header {
    private static final long serialVersionUID = -8520992620053571143L;
    private String name;
    private String value;

    public DefaultHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
