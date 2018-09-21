package site.zido.elise.matcher;

/**
 * match interface
 * @author zido
 */
public interface EliseMatcher {
    /**
     * Determine if it matches the target
     * @param target the target
     * @return true if matches
     */
    boolean matches(Object target);

}
