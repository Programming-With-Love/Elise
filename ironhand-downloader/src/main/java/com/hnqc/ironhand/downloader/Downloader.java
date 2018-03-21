package com.hnqc.ironhand.downloader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class Downloader {
    private static final HttpClient httpClient;
    private String url;

    static {
        httpClient = HttpClients.createDefault();
    }

    public Downloader(String url) {
        this.url = url;
    }

    public void exec(){
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse resp = httpClient.execute(httpGet);
            HttpEntity entity = resp.getEntity();
            String web = EntityUtils.toString(entity,"utf-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
