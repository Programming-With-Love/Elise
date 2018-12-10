package site.zido.elise.select.api;

/**
 * The interface Value.
 *
 * @author zido
 */
public interface Value {
    /**
     * Save value.
     *
     * @param name the name
     * @return the value
     */
    Value save(String name);

    /**
     * Nullable value.
     *
     * @param nullable the nullable
     * @return the value
     */
    Value nullable(boolean nullable);
}
