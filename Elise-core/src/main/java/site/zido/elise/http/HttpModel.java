package site.zido.elise.http;

import java.io.Serializable;
import java.util.List;

public interface HttpModel extends Serializable {
    List<Header> getHeaders(String key);

    List<Header> getAllHeaders();

}
