package site.zido.elise.task.model;

import java.util.List;

public class Action {
    private String source;
    private String token;
    private Object[] extra;
    private List<Action> nextActions;

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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<Action> getNextActions() {
        return nextActions;
    }

    public void setNextActions(List<Action> nextActions) {
        this.nextActions = nextActions;
    }
}
