package site.zido.elise.test.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ResourcesUtils {
    public static InputStream get(String path){
        try {
            return new FileInputStream(ResourcesUtils.class.getClassLoader().getResource(path).getFile());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
