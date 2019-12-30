package com.delta.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * zuul.filter.root用来指定动态加载的过滤器存储路径
 * zuul.filter.interval用来配置动态加载的时间,以秒为单位
 */
@ConfigurationProperties("zuul.filter")
public class FilterConfiguration {
    private String root;
    private Integer interval;

    public FilterConfiguration() {
    }

    public FilterConfiguration(String root, Integer interval) {
        this.root = root;
        this.interval = interval;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    @Override
    public String toString() {
        return "FilterConfiguration{" +
                "root='" + root + '\'' +
                ", interval=" + interval +
                '}';
    }
}
