package com.hnqc.ironhand.common.query;

import com.hnqc.ironhand.common.pojo.UrlRule;

import java.util.List;

public class UrlQuery {
    private List<UrlRule> urlRules;
    private List<UrlRule> scriptRules;
    private List<String> cssSelectors;
    private List<String> xPaths;
    private List<String> savedCssSelectors;
    private List<String> savedXPaths;

    public List<UrlRule> getUrlRules() {
        return urlRules;
    }

    public void setUrlRules(List<UrlRule> urlRules) {
        this.urlRules = urlRules;
    }

    public List<UrlRule> getScriptRules() {
        return scriptRules;
    }

    public void setScriptRules(List<UrlRule> scriptRules) {
        this.scriptRules = scriptRules;
    }

    public List<String> getCssSelectors() {
        return cssSelectors;
    }

    public void setCssSelectors(List<String> cssSelectors) {
        this.cssSelectors = cssSelectors;
    }

    public List<String> getXPaths() {
        return xPaths;
    }

    public void setXPaths(List<String> xPaths) {
        this.xPaths = xPaths;
    }

    public List<String> getSavedCssSelectors() {
        return savedCssSelectors;
    }

    public void setSavedCssSelectors(List<String> savedCssSelectors) {
        this.savedCssSelectors = savedCssSelectors;
    }

    public List<String> getSavedXPaths() {
        return savedXPaths;
    }

    public void setSavedXPaths(List<String> savedXPaths) {
        this.savedXPaths = savedXPaths;
    }
}
