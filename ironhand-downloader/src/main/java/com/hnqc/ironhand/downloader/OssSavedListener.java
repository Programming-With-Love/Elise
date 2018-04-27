package com.hnqc.ironhand.downloader;

import com.aliyun.oss.OSSClient;
import com.hnqc.ironhand.common.SavedPage;
import com.hnqc.ironhand.utils.DateUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.util.Date;

/**
 * OssSavedListener
 *
 * @author zido
 * @date 2018/04/27
 */
public class OssSavedListener implements SavedPage.SavedListener {
    private final static String ENDPOINT = "http://oss-cn-shanghai.aliyuncs.com";
    private final static String ACCESS_KEY_ID = "LTAIHSe76dor6Z7a";
    private final static String ACCESS_KEY_SECRET = "f8Tn6W3Qi3lrgGrsBd4RCujq8juTGb";

    private final static OSSClient ossClient;

    static {
        ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
    }

    @Override
    public String onSave(byte[] rawText) {
        String day = DateUtils.format(new Date(), "yyyyMMdd");
        String key = "html/" + day + "/" + System.currentTimeMillis()
                + String.valueOf(Math.random()).substring(2, 7) + ".html";
        ossClient.putObject("hnqc1", key, new BufferedInputStream(new ByteArrayInputStream(rawText)));
        return "http://static.scustartup.com/" + key;
    }
}
