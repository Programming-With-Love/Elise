package com.hnqc.ironhand.downloader.service.impl;

import com.hnqc.ironhand.downloader.exceptions.DownloadException;
import com.hnqc.ironhand.downloader.service.IDonwloadService;
import com.hnqc.ironhand.downloader.service.IFileService;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DownloadService implements IDonwloadService {
    private IFileService fileService;
    private HttpClient httpClient;

    @Autowired
    public DownloadService(IFileService fileService) {
        this.fileService = fileService;
        httpClient = HttpClients.createDefault();
    }

    @Override
    public void download(String url) {
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse resp = httpClient.execute(httpGet);
            StatusLine line = resp.getStatusLine();
            int code = line.getStatusCode();

        } catch (IOException e) {
            throw new DownloadException(String.format("建立http连接异常:[%s]", url));
        }
    }


}
