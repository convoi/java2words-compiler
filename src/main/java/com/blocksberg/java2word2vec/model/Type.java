package com.blocksberg.java2word2vec.model;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.util.List;
import java.util.Objects;

/**
 * @author jh
 */
public class Type {
    private Multiset<Type> dependsOn;
    private String name;
    private List<Type> typeSequence;

    public Type(String name) {
        this.name = name;
    }

    public Type(String packageName, String shortName) {
        this.name = packageName + "." + shortName;
    }

    public Multiset<Type> getDependsOn() {
        if (dependsOn == null) {
            dependsOn = HashMultiset.create();
        }
        return dependsOn;
    }

    public String shortName() {
        return name.substring(name.lastIndexOf('.') + 1);
    }

    public boolean isStarType() {
        return name.endsWith(".*");
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Type type = (Type) o;
        return Objects.equals(name, type.name);
    }

    public String packageName() {
        return name.substring(0, name.lastIndexOf('.'));
    }

    @Override
    public String toString() {
        return name;
    }

    public String fullQualifiedName() {
        return name;
    }
}
