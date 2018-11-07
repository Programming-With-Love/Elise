package site.zido.elise.event;

import java.util.EventObject;

public abstract class Event<T> extends EventObject {
    private static final long serialVersionUID = 4585934594628317L;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public Event(T source) {
        super(source);
    }
}
