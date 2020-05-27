package site.zido.elise.task.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action action = (Action) o;
        return Objects.equals(token, action.token) &&
            Arrays.equals(extras, action.extras) &&
            Objects.equals(source, action.source) &&
            Objects.equals(children, action.children);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(token, source, children);
        result = 31 * result + Arrays.hashCode(extras);
        return result;
    }
}
