package site.zido.elise.select;

import java.io.Serializable;

class Paragraph implements Serializable {
    private static final long serialVersionUID = 2429211411138531885L;
    private RichType type;
    private String raw;

    public Paragraph() {

    }

    public Paragraph(String raw, RichType type) {
        this.type = type;
        this.raw = raw;
    }

    public RichType getType() {
        return type;
    }

    public void setType(RichType type) {
        this.type = type;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    @Override
    public String toString() {
        return type.name() + ":" + raw;
    }
}
