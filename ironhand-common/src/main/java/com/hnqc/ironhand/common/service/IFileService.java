package com.hnqc.ironhand.common.service;

import java.io.*;

public interface IFileService {
    //写入文件
    String write(InputStream inputStream, String suffix);

    default String writeHtml(String html) throws UnsupportedEncodingException {
        return write(new BufferedInputStream(new ByteArrayInputStream(html.getBytes("utf-8"))), "html");
    }

    default String writeJs(String js) throws UnsupportedEncodingException {
        return write(new BufferedInputStream(new ByteArrayInputStream(js.getBytes("utf-8"))), "js");
    }

    default String writeImg(byte[] bytes) {
        return write(new BufferedInputStream(new ByteArrayInputStream(bytes)), "jpg");
    }

    byte[] read(String url) throws IOException;

    InputStream readAsStream(String url) throws IOException;
}
