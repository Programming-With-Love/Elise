package com.hnqc.ironhand.downloader.exceptions;

import com.hnqc.ironhand.Spider;
import com.hnqc.ironhand.common.SimpleRedisDuplicationProcessor;
import com.hnqc.ironhand.common.SpringKafkaTaskScheduler;
import com.hnqc.ironhand.downloader.HttpClientDownloader;
import com.hnqc.ironhand.scheduler.NoDepuplicationProcessor;
import com.hnqc.ironhand.scheduler.SimpleTaskScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * DownloaderClient
 *
 * @author zido
 * @date 2018/04/23
 */
public class DownloaderClient {
    private final Spider spider;
    private Logger logger = LoggerFactory.getLogger(DownloaderClient.class);

    private final static String REDIS_URL = "redisUrl";
    private final static String KAFKA_SERVERS = "kafkaServers";

    public DownloaderClient() {
        Properties properties = new Properties();
        try {
            InputStream stream = this.getClass().getResourceAsStream("/config.properties");
            if (stream == null) {
                throw new RuntimeException("config.properties load failed");
            }
            properties.load(stream);
        } catch (IOException e) {
            logger.error("config.properties load failed");
            throw new RuntimeException("加载客户端配置信息失败");
        }
        logger.info("connect to redis {}", properties.getProperty(REDIS_URL));
        logger.info("connect to kafka {}", properties.getProperty(KAFKA_SERVERS));
        spider = new Spider(
                new SpringKafkaTaskScheduler(
                        new SimpleTaskScheduler(new NoDepuplicationProcessor()).setPoolSize(10),
                        new SimpleRedisDuplicationProcessor(properties.getProperty(REDIS_URL)))
                        .setBootstrapServers(properties.getProperty(KAFKA_SERVERS))
                        .setSavedListener(new OssSavedListener()),
                null,
                new HttpClientDownloader());
    }

    public void start() {
        logger.info("downloader client start");
        this.spider.start();
    }

    public static void main(String[] args) {
        new DownloaderClient().start();
    }
}
