package site.zido.elise.select.api;

import site.zido.elise.select.matcher.Matcher;
import site.zido.elise.select.matcher.NumberExpressMatcher;

public interface TargetDescriptor {
    //TODO support html?
//    Matchable<TargetDescriptor> html();

    TargetDescriptor matchUrl(Matcher matcher);

    TargetDescriptor statusCode(NumberExpressMatcher matcher);
}
