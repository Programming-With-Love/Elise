package site.zido.elise.select.api.impl;

import site.zido.elise.select.api.body.Bytes;

public class DefaultBytes implements Bytes {
    private byte[] bytes;

    public DefaultBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public Bytes saveBytes() {
        return null;
    }
}
