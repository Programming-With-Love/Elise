package site.zido.elise.http;

/**
 * The type Pair.
 *
 * @author zido
 */
public class Pair {
    private String name;
    private String value;

    /**
     * Instantiates a new Pair.
     */
    public Pair() {

    }

    /**
     * Instantiates a new Pair.
     *
     * @param name  the name
     * @param value the value
     */
    public Pair(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     * @return the name
     */
    public Pair setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     * @return the value
     */
    public Pair setValue(String value) {
        this.value = value;
        return this;
    }
}
