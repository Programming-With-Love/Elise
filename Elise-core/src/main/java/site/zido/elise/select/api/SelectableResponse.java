package site.zido.elise.select.api;

public interface SelectableResponse {
    SelectableHeader header();

    Text cookie();

    Code statusCode();

    Text url();

    SelectableBody body();
}
