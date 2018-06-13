package site.zido.elise.downloader;

import com.aliyun.oss.OSSClient;
import site.zido.elise.common.SavedPage;
import site.zido.elise.utils.DateUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
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
                            String AccessKeySecret,
                            String bucketName,
                            String urlHead) {
        this.bucketName = bucketName;
        this.urlHead = urlHead;
        ossClient = new OSSClient(endPoint, accessKeyId, AccessKeySecret);
    }

    @Override
    public String onSave(byte[] rawText) {
        String day = DateUtils.format(new Date(), "yyyyMMdd");
        String key = "html/" + day + "/" + System.currentTimeMillis()
                + String.valueOf(Math.random()).substring(2, 7) + ".html";
        ossClient.putObject(this.bucketName, key, new BufferedInputStream(new ByteArrayInputStream(rawText)));
        return this.urlHead + key;
    }
}
