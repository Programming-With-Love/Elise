package com.hnqc.ironhand.common.pojo;

import com.hnqc.ironhand.DistributedTask;
import com.hnqc.ironhand.Request;
import com.hnqc.ironhand.common.SavedPage;

/**
 * SavedSeed
 *
 * @author zido
 * @date 2018/04/27
 */
public class SavedSeed extends Seed {
    private SavedPage savedPage;

    public SavedSeed(DistributedTask task, Request request, SavedPage savedPage) {
        super(task, request, null);
        this.savedPage = savedPage;
    }

    public SavedSeed() {

    }

    public SavedPage getSavedPage() {
        return savedPage;
    }

    public SavedSeed setSavedPage(SavedPage savedPage) {
        this.savedPage = savedPage;
        return this;
    }
}
