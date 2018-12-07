package site.zido.elise.http;

public class Pair {
    private String name;
    private String value;

    public Pair() {

    }

    public Pair(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Pair setName(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return value;
    }

    public Pair setValue(String value) {
        this.value = value;
        return this;
    }
}
