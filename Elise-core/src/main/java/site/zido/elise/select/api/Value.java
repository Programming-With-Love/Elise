package site.zido.elise.select.api;

public interface Value {
    Value save(String name);
    Value nullable(boolean nullable);
}
