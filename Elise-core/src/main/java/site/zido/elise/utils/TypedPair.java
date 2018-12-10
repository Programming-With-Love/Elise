package site.zido.elise.utils;

public class TypedPair<K, V> {
    private K key;
    private V value;

    public TypedPair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
