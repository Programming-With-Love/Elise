package site.zido.elise.select.api;

/**
 * The interface Selectable response.
 *
 * @author zido
 */
public interface SelectableResponse {

    /**
     * Model name selectable response.
     *
     * @param name the name
     * @return the selectable response
     */
    SelectableResponse modelName(String name);

    /**
     * As target target descriptor.
     *
     * @return the target descriptor
     */
    TargetDescriptor asTarget();

    /**
     * As helper help descriptor.
     *
     * @return the help descriptor
     */
    HelpDescriptor asHelper();

    /**
     * As content data descriptor.
     *
     * @return the data descriptor
     */
    DataDescriptor asContent();
}
