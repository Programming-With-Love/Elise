package site.zido.elise.utils;

public class Safe {
    private Safe() {
        throw new AssertionError("No Safe instances for you!");
    }

    public static Object getFromArray(Object[] objs, int index) {
        if (index >= objs.length) {
            return null;
        }
        return objs[index];
    }

    public static int getIntFromArray(Object[] objs, int index) {
        return getIntFromArray(objs, index, 0);
    }

    public static int getIntFromArray(Object[] objs, int index, int defaultValue) {
        Object obj = getFromArray(objs, index);
        if (obj instanceof Integer) {
            return (int) obj;
        }
        return defaultValue;
    }

    public static String getStrFromArray(Object[] objs, int index) {
        return getStrFromArray(objs, index, "");
    }

    public static String getStrFromArray(Object[] objs, int index, String defaultValue) {
        Object obj = getFromArray(objs, index);
        if (obj instanceof String) {
            return (String) obj;
        }
        return defaultValue;
    }
}
