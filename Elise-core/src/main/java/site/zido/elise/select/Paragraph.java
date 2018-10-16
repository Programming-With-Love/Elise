package site.zido.elise.select;

class Paragraph {
    private RichType type;
    private String raw;

    public Paragraph(String raw, RichType type) {
        this.type = type;
        this.raw = raw;
    }

    @Override
    public String toString() {
        return type.name() + ":" + raw;
    }
}
