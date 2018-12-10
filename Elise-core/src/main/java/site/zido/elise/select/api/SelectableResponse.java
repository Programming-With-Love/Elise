package site.zido.elise.select.api;

public interface SelectableResponse {

    SelectableResponse modelName(String name);

    TargetDescriptor asTarget();

    HelpDescriptor asHelper();

    DataDescriptor asContent();
}
