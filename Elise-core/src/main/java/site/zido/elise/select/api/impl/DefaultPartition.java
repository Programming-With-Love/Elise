package site.zido.elise.select.api.impl;

import site.zido.elise.select.ElementSelector;
import site.zido.elise.select.FieldType;
import site.zido.elise.select.api.ElementSelectable;
import site.zido.elise.select.api.ElementValue;
import site.zido.elise.select.api.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Default partition.
 *
 * @author zido
 */
public class DefaultPartition implements ElementSelectable {
    private ElementSelector regionSelector;
    private List<Pack> packs;

    /**
     * Gets region selector.
     *
     * @return the region selector
     */
    public ElementSelector getRegionSelector() {
        return regionSelector;
    }

    /**
     * The type Pack.
     *
     * @author zido
     */
    public static class Pack implements ElementValue, Value {
        private ElementSelector fieldSelector;
        private FieldType valueType;
        private String name;
        private boolean nullable;

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

        @Override
        public Value save(String name) {
            this.name = name;
            return this;
        }

        @Override
        public Value nullable(boolean nullable) {
            this.nullable = nullable;
            return this;
        }

        /**
         * Gets nullable.
         *
         * @return the nullable
         */
        public boolean getNullable() {
            return nullable;
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
         * Gets value type.
         *
         * @return the value type
         */
        public FieldType getValueType() {
            return valueType;
        }

        /**
         * Gets name.
         *
         * @return the name
         */
        public String getName() {
            return name;
        }
    }

    /**
     * Instantiates a new Default partition.
     *
     * @param fieldSelector the field selector
     */
    DefaultPartition(ElementSelector fieldSelector) {
        this.regionSelector = fieldSelector;
    }

    @Override
    public ElementSelectable partition(ElementSelector elementSelector) {
        this.regionSelector = elementSelector;
        return this;
    }

    @Override
    public ElementValue select(ElementSelector selector) {
        if (packs == null) {
            packs = new ArrayList<>();
        }
        Pack pack = new Pack();
        packs.add(pack);
        pack.fieldSelector = selector;
        return pack;
    }

    /**
     * Gets packs.
     *
     * @return the packs
     */
    public List<Pack> getPacks() {
        return packs;
    }
}
