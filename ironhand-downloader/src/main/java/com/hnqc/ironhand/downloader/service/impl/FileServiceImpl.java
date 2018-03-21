package com.hnqc.ironhand.downloader.service.impl;

import com.hnqc.ironhand.common.CommonConfig;
import com.hnqc.ironhand.downloader.exceptions.WriteException;
import com.hnqc.ironhand.downloader.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Calendar;
import java.util.UUID;

@Service
public class FileServiceImpl implements IFileService {
    private String rootFilePath;

    @Override
    public String write(byte[] bytes, String suffix) {
        String path = confuse(suffix);
        File file = createFile(path);
        BufferedOutputStream fos;
        try {
            fos = new BufferedOutputStream(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            throw new WriteException(String.format("文件找不到:[%s]", path));
        }
        try {
            fos.write(bytes);
        } catch (IOException e) {
            throw new WriteException(String.format("写入文件失败:[%s]", path));
        }
        return path;
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
