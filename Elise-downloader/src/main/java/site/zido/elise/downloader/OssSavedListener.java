package site.zido.elise.downloader;

import com.aliyun.oss.OSSClient;
import site.zido.elise.common.SavedPage;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 使用oss转存页面
 *
 * @author zido
 */
public class OssSavedListener implements SavedPage.SavedListener {

    private String bucketName;
    private String urlHead;
    private OSSClient ossClient;

    public OssSavedListener(String endPoint,
                            String accessKeyId,
                            String accessKeySecret,
                            String bucketName,
                            String urlHead) {
        this.bucketName = bucketName;
        this.urlHead = urlHead;
        ossClient = new OSSClient(endPoint, accessKeyId, accessKeySecret);
    }

    @Override
    public String onSave(byte[] rawText) {
        String day = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String key = "html/" + day + "/" + System.currentTimeMillis()
                + String.valueOf(Math.random()).substring(2, 7) + ".html";
        ossClient.putObject(this.bucketName, key, new BufferedInputStream(new ByteArrayInputStream(rawText)));
        return this.urlHead + key;
    }
}
