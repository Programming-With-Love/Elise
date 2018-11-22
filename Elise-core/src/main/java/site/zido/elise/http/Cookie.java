package site.zido.elise.http;

import java.util.Date;

public interface Cookie extends Header {
    String getPath();

    Date getExpiryDate();

    boolean isSecure();

    String getDomain();
}
