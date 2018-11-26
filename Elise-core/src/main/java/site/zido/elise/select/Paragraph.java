package site.zido.elise.select;

import java.io.Serializable;

/**
 * The type Paragraph.
 *
 * @author zido
 */
class Paragraph implements Serializable {
    private static final long serialVersionUID = 2429211411138531885L;
    private RichType type;
    private String raw;

    /**
     * Instantiates a new Paragraph.
     */
    public Paragraph() {

    }

    /**
     * Instantiates a new Paragraph.
     *
     * @param raw  the raw
     * @param type the type
     */
    public Paragraph(String raw, RichType type) {
        this.type = type;
        this.raw = raw;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public RichType getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(RichType type) {
        this.type = type;
    }

    /**
     * Gets raw.
     *
     * @return the raw
     */
    public String getRaw() {
        return raw;
    }

    /**
     * Sets raw.
     *
     * @param raw the raw
     */
    public void setRaw(String raw) {
        this.raw = raw;
    }

    @Override
    public String toString() {
        return type.name() + ":" + raw;
    }
}
