package site.zido.elise.select;

/**
 * LinkProperty
 *
 * @author zido
 */
public class LinkProperty {
    private String tag;
    private String attr;

    public LinkProperty() {

    }

    public LinkProperty(String tag, String attr) {
        this.tag = tag;
        this.attr = attr;
    }

    public String getTag() {
        return tag;
    }

    public LinkProperty setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public String getAttr() {
        return attr;
    }

    public LinkProperty setAttr(String attr) {
        this.attr = attr;
        return this;
    }
}
