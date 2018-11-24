package site.zido.elise.custom;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public interface Config {
    void from(Reader reader) throws IOException;

    void from(InputStream in) throws IOException;

    void set(String key, Object value);

    <T> T get(String key);
}
