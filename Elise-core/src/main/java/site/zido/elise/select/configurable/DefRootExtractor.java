package site.zido.elise.select.configurable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 根抽取器描述
 * <p>
 * 可做为model配置，此时{@link #name} 为model名，e.g：当使用mysql时，为表名
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
 *
 * @author zido
 */
public class DefRootExtractor extends DefExtractor {
    /**
     * 目标url
     */
    private List<ConfigurableUrlFinder> targetUrl;

    /**
     * 辅助url
     */
    private List<ConfigurableUrlFinder> helpUrl;

    /**
     * 子规则
     */
    private ArrayList<DefExtractor> children;

    /**
     * Instantiates a new Def root extractor.
     */
    public DefRootExtractor() {
        this("default-task");
    }

    /**
     * Instantiates a new Def root extractor.
     *
     * @param name the name
     */
    public DefRootExtractor(String name) {
        super(name);
    }

    /**
     * Gets target url.
     *
     * @return the target url
     */
    public List<ConfigurableUrlFinder> getTargetUrl() {
        return targetUrl;
    }

    /**
     * Sets target url.
     *
     * @param targetUrl the target url
     * @return the target url
     */
    public DefRootExtractor setTargetUrl(List<ConfigurableUrlFinder> targetUrl) {
        this.targetUrl = targetUrl;
        return this;
    }

    /**
     * Gets help url.
     *
     * @return the help url
     */
    public List<ConfigurableUrlFinder> getHelpUrl() {
        return helpUrl;
    }

    /**
     * Sets help url.
     *
     * @param helpUrl the help url
     * @return the help url
     */
    public DefRootExtractor setHelpUrl(List<ConfigurableUrlFinder> helpUrl) {
        this.helpUrl = helpUrl;
        return this;
    }

    /**
     * Add target url def root extractor.
     *
     * @param targetUrl the target url
     * @return the def root extractor
     */
    public DefRootExtractor addTargetUrl(ConfigurableUrlFinder... targetUrl) {
        if (this.targetUrl == null) {
            this.targetUrl = new ArrayList<>();
        }
        if (targetUrl == null) {
            return this;
        }
        this.targetUrl.addAll(Arrays.asList(targetUrl));
        return this;
    }

    /**
     * Add help url def root extractor.
     *
     * @param helpUrl the help url
     * @return the def root extractor
     */
    public DefRootExtractor addHelpUrl(ConfigurableUrlFinder... helpUrl) {
        if (this.helpUrl == null) {
            this.helpUrl = new ArrayList<>();
        }
        if (helpUrl == null) {
            return this;
        }
        this.helpUrl.addAll(Arrays.asList(helpUrl));
        return this;
    }

    /**
     * Gets children.
     *
     * @return the children
     */
    public ArrayList<DefExtractor> getChildren() {
        return children;
    }

    /**
     * Sets children.
     *
     * @param children the children
     * @return the children
     */
    public DefExtractor setChildren(ArrayList<DefExtractor> children) {
        this.children = children;
        return this;
    }

    /**
     * Add children.
     *
     * @param extractor the extractor
     */
    public void addChildren(DefExtractor extractor) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        this.children.add(extractor);
    }
}
