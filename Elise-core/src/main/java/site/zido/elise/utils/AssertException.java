package site.zido.elise.utils;

import java.io.Serializable;

public class AssertException extends RuntimeException implements Serializable {
    public AssertException(String message){
        super(message);
    }
}
