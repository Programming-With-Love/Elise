package site.zido.elise.task.api;

/**
 * The interface Data descriptor.
 *
 * @author zido
 */
public interface DataDescriptor {
    /**
     * Html element selectable.
     *
     * @return the element selectable
     */
    ElementSelectable html();

    /**
     * Url value.
     *
     * @return the value
     */
    Value url();

    /**
     * Status code value.
     *
     * @return the value
     */
    Value statusCode();
}
