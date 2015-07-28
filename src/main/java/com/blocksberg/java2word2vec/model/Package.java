package com.blocksberg.java2word2vec.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jh
 */
public class Package {
    private String name;
    private List<Package> children;
    private List<Type> types;

    public Package(String name) {
        this.children = new ArrayList<>();
        this.types = new ArrayList<>();
    }

    public void addType(Type type) {
        this.types.add(type);
    }

    public void addSubPackage(Package subPackage) {
        this.children.add(subPackage);
    }
}
