package site.zido.elise.task.model;

public final class Target {
    private String from;

    private String token;

    private Object[] extra;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
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
