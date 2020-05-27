package site.zido.elise.downloader;

public class DownloadException extends Exception {
    public DownloadException(String msg) {
        super(msg);
    }

    public DownloadException(Throwable e) {
        super(e);
    }
}
