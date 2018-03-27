package com.hnqc.ironhand.common.exceptions;

public class WriteException extends NestedRuntimeException {
    public WriteException(String msg) {
        super("写入文件异常" + msg);
    }
}
