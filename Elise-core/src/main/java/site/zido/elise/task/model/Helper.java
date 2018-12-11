package site.zido.elise.task.model;

public final class Helper {
    /**
     * where to get it from
     */
    private String source;

    private String token;

    private Object[] extra;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Object[] getExtra() {
        return extra;
    }

    public void setExtra(Object[] extra) {
        this.extra = extra;
    }
}
