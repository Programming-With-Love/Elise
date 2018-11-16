package site.zido.elise.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EventListener;
import java.util.Set;
import java.util.function.Consumer;

public class EventUtils {
    private final static Logger LOGGER = LoggerFactory.getLogger(EventUtils.class);

    private EventUtils() {
    }

    public static <T extends EventListener> void mustNotifyListeners(Set<T> listeners, Consumer<T> callback) {
        for (T listener : listeners) {
            try {
                callback.accept(listener);
            } catch (Throwable e) {
                LOGGER.error("listener callback error", e);
            }
        }
    }

    public static <T extends EventListener> void notifyListeners(Set<T> listeners, Consumer<T> callback) {
        for (T listener : listeners) {
            callback.accept(listener);
        }
    }
}
