package site.zido.elise.event;

public class CancelTaskEvent extends Event<Long> {
    private static final long serialVersionUID = 7653323745401448875L;

    /**
     * cancel task event
     *
     * @param taskId task id
     */
    public CancelTaskEvent(Long taskId) {
        super(taskId);
    }
}
