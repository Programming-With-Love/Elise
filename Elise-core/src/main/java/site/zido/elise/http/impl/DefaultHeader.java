package site.zido.elise.http.impl;

import site.zido.elise.http.Header;

/**
 * The type Default header.
 *
 * @author zido
 */
public class DefaultHeader implements Header {
    private static final long serialVersionUID = -8520992620053571143L;
    private String name;
    private String value;

    /**
     * Instantiates a new Default header.
     *
     * @param name  the name
     * @param value the value
     */
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

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(String value) {
        this.value = value;
    }
}
