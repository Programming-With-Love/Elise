package com.hnqc.ironhand.common;

import com.hnqc.common.image.OssImageUtil;
import com.hnqc.ironhand.Page;
import com.hnqc.ironhand.Request;
import com.hnqc.ironhand.Task;
import com.hnqc.ironhand.common.exceptions.WriteException;
import com.hnqc.ironhand.downloader.AbstractDownloader;
import com.hnqc.ironhand.downloader.Downloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ali Oss Http Downloader
 *
 * @author zido
 * @date 2018/04/20
 */
public class SavedHttpDownloader extends AbstractDownloader {
    private String rootFilePath;
    private Downloader downloader;
    private static  Pattern pattern = Pattern.compile("/\\w+(\\.\\w+)$");

    public SavedHttpDownloader(String rootFilePath, Downloader downloader) {
        this.rootFilePath = rootFilePath;
        this.downloader = downloader;
    }

    public String write(InputStream inputStream, String suffix) {
        suffix = suffix.replace(".", "").trim();
        String path = getPath();
        String name = getName(suffix);
        return OssImageUtil.saveImg(inputStream, path, name);
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
        if (month.length() < 2) {
            month = "0" + month;
        }
        result.append(month).append(File.pathSeparator);
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        if (day.length() < 2) {
            day = "0" + day;
        }
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
        if (month.length() < 2) {
            month = "0" + month;
        }
        result.append(month).append(File.pathSeparator);
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        if (day.length() < 2) {
            day = "0" + day;
        }
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

    @Override
    public Page download(Request request, Task task) {
        String url = request.getUrl();
        Matcher matcher = pattern.matcher(url);
        int result = matcher.start(1);
        System.out.println(url.substring(0,result)+"_html");
        return null;
    }

    @Override
    public void setThread(int threadNum) {
        downloader.setThread(threadNum);
    }
}
