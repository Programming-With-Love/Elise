package site.zido.elise.select.api;

import site.zido.elise.select.api.body.Bytes;
import site.zido.elise.select.api.body.Html;

public interface SelectableBody {
    Html html();

    Bytes bytes();
}
