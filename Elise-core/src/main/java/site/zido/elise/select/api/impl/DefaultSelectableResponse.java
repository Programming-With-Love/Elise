package site.zido.elise.select.api.impl;

import site.zido.elise.select.api.*;

public class DefaultSelectableResponse implements SelectableResponse {
    private SelectableHeader header;
    private Text url;
    private SelectableHeader cookies;
    private Code code;
    private SelectableBody body;

    @Override
    public SelectableHeader header() {
        return null;
    }

    public void setHeader(SelectableHeader header) {
        this.header = header;
    }

    @Override
    public Text cookie() {
        return null;
    }

    @Override
    public Code statusCode() {
        return null;
    }

    @Override
    public Text url() {
        return null;
    }

    @Override
    public SelectableBody body() {
        return null;
    }

    public void setUrl(Text url) {
        this.url = url;
    }

    public void setCookies(DefaultSelectableHeader cookies) {
        this.cookies = cookies;
    }

    public void setCode(DefaultCode code) {
        this.code = code;
    }

    public void setBody(DefaultSelectableBody body) {
        this.body = body;
    }
}
