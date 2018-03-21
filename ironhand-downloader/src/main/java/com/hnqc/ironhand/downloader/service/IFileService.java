package com.hnqc.ironhand.downloader.service;

import java.io.UnsupportedEncodingException;

public interface IFileService {
    //写入文件
    String write(byte[] bytes, String suffix);

    default String writeHtml(String html) throws UnsupportedEncodingException {
        return write(html.getBytes("utf-8"), "html");
    }

    default String writeJs(String js) throws UnsupportedEncodingException {
        return write(js.getBytes("utf-8"), "js");
    }

    default String writeImg(byte[] bytes) {
        return write(bytes, "jpg");
    }
}
