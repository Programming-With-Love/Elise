package site.zido.elise.task.model;

public class Action {
    private String token;
    private Object[] extra;

    public Action(String token, Object[] extra) {
        this.token = token;
        this.extra = extra;
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
