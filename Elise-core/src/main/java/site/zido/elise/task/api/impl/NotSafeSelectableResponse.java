package site.zido.elise.task.api.impl;

import site.zido.elise.select.*;
import site.zido.elise.select.LinkSelectHandler;
import site.zido.elise.task.api.*;
import site.zido.elise.task.api.Source;
import site.zido.elise.select.matcher.Matcher;
import site.zido.elise.select.NumberMatcherSelectHandler;
import site.zido.elise.utils.Asserts;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Not safe selectable response.
 *
 * @author zido
 */
public class NotSafeSelectableResponse implements SelectableResponse,
        TargetDescriptor,
        HelpDescriptor,
        DataDescriptor,
        ElementSelectable,
        Value,
        ElementValue {

    /**
     * The constant MODE_TARGET.
     */
    public static final int MODE_TARGET = 1;
    /**
     * The constant MODE_HELP.
     */
    public static final int MODE_HELP = 2;
    /**
     * The constant MODE_DATA.
     */
    public static final int MODE_DATA = 3;
    private List<NotSafeSelectableResponse> metas = new ArrayList<>();

    private int mode;
    private String name;
    private DefaultPartition fieldPair;
    private ElementSelector fieldSelector;
    private Source source;
    private List<LinkSelectHandler> linkSelectors;
    private List<Matcher> matchers;
    private FieldType valueType;
    private boolean nullable;

    /**
     * Instantiates a new Not safe selectable response.
     */
    public NotSafeSelectableResponse() {

    }

    private NotSafeSelectableResponse(int mode) {
        this.mode = mode;
    }

    @Override
    public SelectableResponse modelName(String name) {
        Asserts.hasLength(name, "name can't be null or empty");
        this.name = name;
        return this;
    }

    @Override
    public TargetDescriptor asTarget() {
        NotSafeSelectableResponse faSelectableResponse = new NotSafeSelectableResponse(MODE_TARGET);
        metas.add(faSelectableResponse);
        return faSelectableResponse;
    }

    @Override
    public HelpDescriptor asHelper() {
        NotSafeSelectableResponse faSelectableResponse = new NotSafeSelectableResponse(MODE_HELP);
        metas.add(faSelectableResponse);
        return faSelectableResponse;
    }

    @Override
    public DataDescriptor asContent() {
        NotSafeSelectableResponse faSelectableResponse = new NotSafeSelectableResponse(MODE_DATA);
        metas.add(faSelectableResponse);
        return faSelectableResponse;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    @Override
    public ElementSelectable html() {
        source = Source.RAW_HTML;
        return this;
    }

    @Override
    public Value url() {
        source = Source.URL;
        return this;
    }

    @Override
    public Value statusCode() {
        source = Source.CODE;
        return this;
    }

    @Override
    public HelpDescriptor filter(LinkSelectHandler linkSelector) {
        Asserts.notNull(linkSelector, "can't filter by null");
        if (linkSelectors == null) {
            linkSelectors = new ArrayList<>();
        }
        this.linkSelectors.add(linkSelector);
        return this;
    }

    @Override
    public TargetDescriptor matchUrl(Matcher matcher) {
        Asserts.notNull(matcher, "can't match by null");
        if (matchers == null) {
            matchers = new ArrayList<>();
        }
        matchers.add(matcher);
        return this;
    }

    @Override
    public TargetDescriptor statusCode(NumberMatcherSelectHandler matcher) {
        Asserts.notNull(matcher, "can't match by null");
        return matchUrl(matcher);
    }

    @Override
    public ElementSelectable partition(ElementSelector elementSelector) {
        Asserts.notNull(elementSelector, "can't partition by null");
        DefaultPartition partition = new DefaultPartition(elementSelector);
        fieldPair = partition;
        return partition;
    }

    @Override
    public ElementValue select(ElementSelector selector) {
        Asserts.notNull(selector, "can't select by null");
        this.fieldSelector = selector;
        return this;
    }

    @Override
    public Value save(String name) {
        Asserts.hasLength(name, "name can't be null or empty");
        if (this.valueType == null) {
            this.valueType = FieldType.ORIGIN;
        }
        this.name = name;
        return this;
    }

    @Override
    public Value nullable(boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    @Override
    public Value text() {
        this.valueType = FieldType.TEXT;
        return this;
    }

    @Override
    public Value rich() {
        this.valueType = FieldType.RICH;
        return this;
    }

    @Override
    public Value xml() {
        this.valueType = FieldType.XML;
        return this;
    }

    /**
     * Gets metas.
     *
     * @return the metas
     */
    public List<NotSafeSelectableResponse> getMetas() {
        return metas;
    }

    /**
     * Gets source.
     *
     * @return the source
     */
    public Source getSource() {
        return source;
    }

    /**
     * Gets link selectors.
     *
     * @return the link selectors
     */
    public List<LinkSelectHandler> getLinkSelectors() {
        return linkSelectors;
    }

    /**
     * Gets matchers.
     *
     * @return the matchers
     */
    public List<Matcher> getMatchers() {
        return matchers;
    }

    /**
     * Gets value type.
     *
     * @return the value type
     */
    public FieldType getValueType() {
        return valueType;
    }

    /**
     * Gets mode.
     *
     * @return the mode
     */
    public int getMode() {
        return mode;
    }

    /**
     * Gets partition.
     *
     * @return the partition
     */
    public DefaultPartition getPartition() {
        return fieldPair;
    }

    /**
     * Gets field selector.
     *
     * @return the field selector
     */
    public ElementSelector getFieldSelector() {
        return fieldSelector;
    }

    /**
     * Gets nullable.
     *
     * @return the nullable
     */
    public boolean getNullable() {
        return nullable;
    }
}
