package site.zido.elise;

import site.zido.elise.events.EventSupport;
import site.zido.elise.select.configurable.DefRootExtractor;

public interface Spider extends EventSupport {
    Operator of(DefRootExtractor extractor);
}
