package site.zido.elise.utils;

import com.sun.istack.internal.Nullable;

public class Asserts {
    public static void notNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object obj) {
        notNull(obj, "[Assertion failed] - the object argument must not be null");
    }

    public static void isNull(Object obj,String message){
        if(obj != null){
            throw new IllegalArgumentException(message);
        }
    }

    public static void isNull(Object obj){
        isNull(obj,"[Assertion failed] - the object argument must be null");
    }

    public static void hasLength(@Nullable String text, String message){
        if(!StringUtils.hasLength(text)){
            throw new IllegalArgumentException(message);
        }
    }

    public static void hasLength(@Nullable String text){
        hasLength(text,"[Assertion failed] - this String argument must have length; it must not be null or empty");
    }

}
