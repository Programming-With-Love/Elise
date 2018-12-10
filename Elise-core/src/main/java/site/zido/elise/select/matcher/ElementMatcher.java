package site.zido.elise.select.matcher;

import org.jsoup.nodes.Document;

/**
 * The interface Element matcher.
 *
 * @author zido
 */
public interface ElementMatcher {
    /**
     * Determine if it matches the target
     *
     * @param target the target
     * @return true if matches
     */
    boolean matches(Document target);
}
