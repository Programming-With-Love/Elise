package site.zido.elise.http;

import java.io.Serializable;

public interface Header extends Serializable {
    String getName();

    String getValue();
}
