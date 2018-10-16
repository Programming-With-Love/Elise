package site.zido.elise.select;

/**
 * match interface
 *
 * @author zido
 */
public interface Matcher {
    /**
     * Determine if it matches the target
     *
     * @param target the target
     * @return true if matches
     */
    boolean matches(Object target);

}
