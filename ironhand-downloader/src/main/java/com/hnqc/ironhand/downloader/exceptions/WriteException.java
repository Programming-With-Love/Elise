package com.hnqc.ironhand.downloader.exceptions;

import com.hnqc.ironhand.common.exceptions.NestedRuntimeException;

public class WriteException extends NestedRuntimeException {
    public WriteException(String msg) {
        super("写入文件异常" + msg);
    }
}
