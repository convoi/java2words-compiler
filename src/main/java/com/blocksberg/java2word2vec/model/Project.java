package com.blocksberg.java2word2vec.model;

import java.util.List;

/**
 * @author jh
 */
public class Project {
    private final String name;
    private final List<String> metrics;

    public Project(String name, List<String> metrics) {
        this.name = name;
        this.metrics = metrics;
    }

    public String getName() {
        return name;
    }

    public List<String> getMetrics() {
        return metrics;
    }
}
