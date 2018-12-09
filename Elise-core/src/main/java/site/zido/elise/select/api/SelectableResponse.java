package site.zido.elise.select.api;

public interface SelectableResponse {
    SelectableHeader header();

    Text cookie();

    StatusCode statusCode();

    Text url();

    SelectableBody body();
}
