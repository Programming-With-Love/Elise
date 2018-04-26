package com.hnqc.ironhand.downloader.exceptions;

import com.hnqc.common.image.OssImageUtil;
import com.hnqc.ironhand.common.SavedPage;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;

/**
 * OssSavedListener
 *
 * @author zido
 * @date 2018/04/27
 */
public class OssSavedListener implements SavedPage.SavedListener {
    @Override
    public String onSave(byte[] rawText) {
        return OssImageUtil.saveImg(new BufferedInputStream(new ByteArrayInputStream(rawText)), "");
    }
}
