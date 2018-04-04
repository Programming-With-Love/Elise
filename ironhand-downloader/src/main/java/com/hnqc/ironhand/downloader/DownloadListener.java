package com.hnqc.ironhand.downloader;

import com.hnqc.common.image.OssImageUtil;
import com.hnqc.ironhand.common.pojo.UrlEntry;
import com.hnqc.ironhand.common.pojo.Seed;
import com.hnqc.ironhand.common.sender.AnalyzerSender;
import com.hnqc.ironhand.common.utils.ValidateUtils;
import com.hnqc.ironhand.downloader.service.IDownloadService;
import com.hnqc.ironhand.common.service.IFileService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Component
public class DownloadListener {
    private String groupId = "download";
    private String topic = "download";

    private IFileService fileService;
    private IDownloadService downloadService;

    private AnalyzerSender analyzerSender;

    private static Logger logger = LoggerFactory.getLogger(DownloadListener.class);

    @Autowired
    public DownloadListener(IFileService fileService, IDownloadService downloadService) {
        this.fileService = fileService;
        this.downloadService = downloadService;
    }

    @KafkaListener(topics = "#{__listener.topic}", groupId = "#{__listener.groupId}")
    public void listen(ConsumerRecord<Integer, Seed> record) {
        Seed seed = record.value();
        List<UrlEntry> html = seed.getUrls();
        for (UrlEntry urlEntry : html) {
            String name = urlEntry.getName();
            String value = urlEntry.getValue();
            if (ValidateUtils.isEmpty(value)) {
                urlEntry.setTryTimes(urlEntry.getTryTimes() + 1);
                InputStream result = downloadService.getStream(name);
                try {
                    String path = fileService.writeHtml(result);
                    urlEntry.setValue(path);
                } catch (UnsupportedEncodingException e) {
                    logger.error("下载出错,编码不支持", e);
                }
            }
        }
        List<UrlEntry> scripts = seed.getScripts();
        for (UrlEntry urlEntry : scripts) {
            String name = urlEntry.getName();
            String value = urlEntry.getValue();
            if (ValidateUtils.isEmpty(value)) {
                urlEntry.setTryTimes(urlEntry.getTryTimes() + 1);
                String result = downloadService.download(name);
                try {
                    String path = fileService.writeJs(result);
                    urlEntry.setValue(path);
                } catch (UnsupportedEncodingException e) {
                    logger.error("下载出错，编码不支持", e);
                }
            }
        }

        List<UrlEntry> images = seed.getImages();
        for (UrlEntry urlEntry : images) {
            String name = urlEntry.getName();
            String value = urlEntry.getValue();
            if (ValidateUtils.isEmpty(value)) {
                urlEntry.setTryTimes(urlEntry.getTryTimes() + 1);
                InputStream stream = downloadService.getStream(name);
                String s = OssImageUtil.saveImg(stream, name.substring(name.lastIndexOf(".")));
                urlEntry.setValue(s);
            }
        }

        analyzerSender.send(seed);
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Autowired
    public void setAnalyzerSender(AnalyzerSender analyzerSender) {
        this.analyzerSender = analyzerSender;
    }
}
