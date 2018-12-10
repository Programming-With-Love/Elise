package site.zido.elise.select.api;

/**
 * The interface Rich result.
 *
 * @author zido
 */
public interface RichResult extends SelectResult {
    /**
     * Save as rich text rich result.
     *
     * @return the rich result
     */
    RichResult saveAsRichText();
}
