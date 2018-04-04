package com.hnqc.ironhand.common.service.impl;

import com.hnqc.common.image.OssImageUtil;
import com.hnqc.ironhand.common.exceptions.WriteException;
import com.hnqc.ironhand.common.service.IFileService;
import com.hnqc.ironhand.utils.CommonConfig;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.UUID;

@Service
public class FileServiceImpl implements IFileService {
    private String rootFilePath;
    private HttpClient httpClient;

    public FileServiceImpl() {
        httpClient = HttpClients.createDefault();
    }

    @Override
    public String write(InputStream inputStream, String suffix) {
        suffix = suffix.replace(".", "").trim();
        String path = getPath();
        String name = getName(suffix);
        return OssImageUtil.saveImg(inputStream, path, name);
    }

    @Override
    public byte[] read(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        HttpResponse resp = httpClient.execute(httpGet);
        HttpEntity entity = resp.getEntity();
        return EntityUtils.toByteArray(entity);
    }

    @Override
    public InputStream readAsStream(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        HttpResponse resp = httpClient.execute(httpGet);
        HttpEntity entity = resp.getEntity();
        return entity.getContent();
    }

    @Autowired
    public void setConfig(CommonConfig config) {
        this.rootFilePath = config.getRootFilePath();
    }

    /**
     * 通过时间分目录，使用uuid作为文件名
     *
     * @param suffix 后缀名
     */
    private String confuse(String suffix) {
        StringBuilder result = new StringBuilder(rootFilePath + File.pathSeparator);
        Calendar calendar = Calendar.getInstance();
        result.append(calendar.get(Calendar.YEAR)).append(File.pathSeparator);
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        if (month.length() < 2)
            month = "0" + month;
        result.append(month).append(File.pathSeparator);
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        if (day.length() < 2)
            day = "0" + day;
        result.append(day).append(File.pathSeparator);
        result.append(UUID.randomUUID());
        result.append(".").append(suffix);
        return result.toString();
    }

    private String getPath() {
        StringBuilder result = new StringBuilder(rootFilePath + File.pathSeparator);
        Calendar calendar = Calendar.getInstance();
        result.append(calendar.get(Calendar.YEAR)).append(File.pathSeparator);
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        if (month.length() < 2)
            month = "0" + month;
        result.append(month).append(File.pathSeparator);
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        if (day.length() < 2)
            day = "0" + day;
        result.append(day).append(File.pathSeparator);
        return result.toString();
    }

    private String getName(String suffix) {
        return String.valueOf(UUID.randomUUID()) +
                "." + suffix;
    }

    private File createFile(String wholePath) {
        File file = new File(wholePath);
        File dir = file.getParentFile();
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new WriteException(String.format("目录创建失败[%s]", dir.getPath()));
            }
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new WriteException(String.format("文件创建失败[%s]", wholePath));
            }
        }
        return file;
    }
}
