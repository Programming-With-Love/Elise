package com.hnqc.ironhand.spider.distributed.configurable;

import java.util.ArrayList;

/**
 * 抽取器配置类
 * <p>
 * 配置抽取内容的规则
 * <p>
 * 可做为model配置，此时{@link #name} 为model名，e.g：当使用mysql时，为表明
 * <p>
 * 可做为单独的每条属性配置，此时{@link #name} 为字段名 <br>
 * e.g:
 * <code>
 * {
 * value:"div.class",
 * type:"CSS",
 * children:[{
 * value:"div.class",
 * name:"title",
 * },{
 * value:"div.class",
 * name:"content",
 * }]
 * }
 * </code>
 * </p>
 *
 * @author zido
 * @date 2018/28/12
 */
public class DefExtractor {
    /**
     * 字段名
     */
    private String name;

    /**
     * 子规则
     */
    private ArrayList<DefExtractor> children;

    /**
     * 抽取值，默认为xpath跟路径
     */
    private String value = "//";
    /**
     * 抽取值类型，默认为xpath
     */
    private ExpressionType type = ExpressionType.XPATH;
    /**
     * 是否多条 ,默认为单条
     */
    private Boolean multi = false;
    /**
     * 是否允许空 ,默认允许为空
     */
    private Boolean nullable = true;



    public String getName() {
        return name;
    }

    public DefExtractor setName(String name) {
        this.name = name;
        return this;
    }

    public ArrayList<DefExtractor> getChildren() {
        return children;
    }

    public DefExtractor setChildren(ArrayList<DefExtractor> children) {
        this.children = children;
        return this;
    }

    public String getValue() {
        return value;
    }

    public DefExtractor setValue(String value) {
        this.value = value;
        return this;
    }

    public ExpressionType getType() {
        return type;
    }

    public DefExtractor setType(ExpressionType type) {
        this.type = type;
        return this;
    }

    public Boolean getMulti() {
        return multi;
    }

    public DefExtractor setMulti(Boolean multi) {
        this.multi = multi;
        return this;
    }

    public Boolean getNullable() {
        return nullable;
    }

    public DefExtractor setNullable(Boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    public void addChildren(DefExtractor extractor) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        this.children.add(extractor);
    }
}
