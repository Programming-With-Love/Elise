package site.zido.elise.select;

/**
 * LinkProperty
 *
 * @author zido
 */
public class LinkProperty {
    private String tag;
    private String attr;

    /**
     * Instantiates a new Link property.
     */
    public LinkProperty() {

    }

    /**
     * Instantiates a new Link property.
     *
     * @param tag  the tag
     * @param attr the attr
     */
    public LinkProperty(String tag, String attr) {
        this.tag = tag;
        this.attr = attr;
    }

    /**
     * Gets tag.
     *
     * @return the tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * Sets tag.
     *
     * @param tag the tag
     * @return the tag
     */
    public LinkProperty setTag(String tag) {
        this.tag = tag;
        return this;
    }

    /**
     * Gets attr.
     *
     * @return the attr
     */
    public String getAttr() {
        return attr;
    }

    /**
     * Sets attr.
     *
     * @param attr the attr
     * @return the attr
     */
    public LinkProperty setAttr(String attr) {
        this.attr = attr;
        return this;
    }
}
