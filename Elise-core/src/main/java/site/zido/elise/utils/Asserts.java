package site.zido.elise.utils;

import java.util.Collection;

/**
 * Assertion tool.
 *
 * @author zido
 */
public class Asserts {
    /**
     * Not null.
     *
     * @param obj     the obj
     * @param message the message
     */
    public static void notNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Not null.
     *
     * @param obj the obj
     */
    public static void notNull(Object obj) {
        notNull(obj, "[Assertion failed] - the object argument must not be null");
    }

    /**
     * Is null.
     *
     * @param obj     the obj
     * @param message the message
     */
    public static void isNull(Object obj, String message) {
        if (obj != null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Is null.
     *
     * @param obj the obj
     */
    public static void isNull(Object obj) {
        isNull(obj, "[Assertion failed] - the object argument must be null");
    }

    /**
     * Has length.
     *
     * @param text    the text
     * @param message the message
     */
    public static void hasLength(String text, String message) {
        if (!StringUtils.hasLength(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Has length.
     *
     * @param text the text
     */
    public static void hasLength(String text) {
        hasLength(text, "[Assertion failed] - this String argument must have length; it must not be null or empty");
    }

    /**
     * Not empty.
     *
     * @param collection the collection
     */
    public static void notEmpty(Collection<?> collection) {
        notEmpty(collection, "[Assertion failed] - this collection argument must be not null or empty");
    }

    /**
     * Not empty.
     *
     * @param collection the collection
     * @param message    the message
     */
    public static void notEmpty(Collection<?> collection, String message) {
        if (ValidateUtils.isEmpty(collection)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Not empty.
     *
     * @param array   the array
     * @param message the message
     */
    public static void notEmpty(Object[] array, String message) {
        if (ValidateUtils.isEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Not empty.
     *
     * @param array the array
     */
    public static void notEmpty(Object[] array) {
        notEmpty(array, "[Assertion failed] - this array must not be empty: it must contain at least 1 element");
    }
}
