package site.zido.elise.custom;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

/**
 * the config interface
 *
 * @author zido
 */
public interface Config extends Map<String, Object> {

    /**
     * parse from reader.
     *
     * @param reader the reader
     * @throws IOException the io exception
     */
    void from(Reader reader) throws IOException;

    /**
     * parse from input stream
     *
     * @param in the in
     * @throws IOException the io exception
     */
    void from(InputStream in) throws IOException;

    /**
     * set the key value pair
     *
     * @param key   the key
     * @param value the value
     */
    void set(String key, Object value);

    /**
     * get the value by key
     *
     * @param <T> the type parameter
     * @param key the key
     * @return the t
     */
    <T> T get(String key);
}
