package site.zido.elise.task.api;

/**
 * The interface Element value.
 *
 * @author zido
 */
public interface ElementValue {
    /**
     * Text value.
     *
     * @return the value
     */
    Value text();

    /**
     * Rich value.
     *
     * @return the value
     */
    Value rich();

    /**
     * Xml value.
     *
     * @return the value
     */
    Value xml();
}
