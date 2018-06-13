package site.zido.elise;

/**
 * Task interface
 *
 * @author zido
 */
public interface Task {
    /**
     * Get task id
     *
     * @return id
     */
    Long getId();

    /**
     * Get website configuration
     *
     * @return site
     */
    Site getSite();
}
