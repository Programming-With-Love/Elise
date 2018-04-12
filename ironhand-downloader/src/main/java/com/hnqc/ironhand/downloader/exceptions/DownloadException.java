package com.hnqc.ironhand.downloader.exceptions;

import com.hnqc.ironhand.common.exceptions.AbstractNestedRuntimeException;

public class DownloadException extends AbstractNestedRuntimeException {
    public DownloadException(String msg) {
        super("下载异常：" + msg);
    }
}
