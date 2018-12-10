package site.zido.elise.utils;

/**
 * The type Typed pair.
 *
 * @param <K> the type parameter
 * @param <V> the type parameter
 * @author zido
 */
public class TypedPair<K, V> {
    private K key;
    private V value;

    /**
     * Instantiates a new Typed pair.
     *
     * @param key   the key
     * @param value the value
     */
    public TypedPair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Gets key.
     *
     * @return the key
     */
    public K getKey() {
        return key;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public V getValue() {
        return value;
    }
}
