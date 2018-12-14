package site.zido.elise.task.model;

import java.util.List;

public class Action {
    private String token;
    private Object[] extras;
    private String source;
    private List<Action> children;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Object[] getExtras() {
        return extras;
    }

    public void setExtras(Object[] extras) {
        this.extras = extras;
    }

    public List<Action> getChildren() {
        return children;
    }

    public void setChildren(List<Action> children) {
        this.children = children;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
