package site.zido.elise.http;

import site.zido.elise.select.Text;

public interface Response extends HttpModel {
    int getStatusCode();

    String getReasonPhrase();

    Text getUrl();
}
