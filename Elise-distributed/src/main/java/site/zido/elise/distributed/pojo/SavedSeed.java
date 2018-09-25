package site.zido.elise.distributed.pojo;

import site.zido.elise.DefaultExtractorTask;
import site.zido.elise.Request;
import site.zido.elise.distributed.SavedPage;

/**
 * SavedSeed
 *
 * @author zido
 */
public class SavedSeed extends Seed {
    private SavedPage savedPage;

    public SavedSeed(DefaultExtractorTask task, Request request, SavedPage savedPage) {
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
