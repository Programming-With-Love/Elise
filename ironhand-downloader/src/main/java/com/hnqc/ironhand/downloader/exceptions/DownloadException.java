package com.hnqc.ironhand.downloader.exceptions;

import com.hnqc.ironhand.common.exceptions.NestedRuntimeException;

public class DownloadException extends NestedRuntimeException {
    public DownloadException(String msg) {
        super("下载异常：" + msg);
    }
}
