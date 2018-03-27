package com.hnqc.ironhand.downloader.service;

import java.io.InputStream;

public interface IDownloadService {
    String download(String url);

    InputStream getStream(String url);
}
