package com.hnqc.ironhand.common.exceptions;

public class WriteException extends AbstractNestedRuntimeException {
    public WriteException(String msg) {
        super("写入文件异常" + msg);
    }
}
