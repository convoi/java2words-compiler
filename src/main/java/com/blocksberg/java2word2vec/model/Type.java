package com.blocksberg.java2word2vec.model;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author jh
 */
public class Type {
    private Multiset<Type> dependsOn;
    private String name;
    private List<Method> methods;
    private List<Field> fields;
    private Integer clusterId;

    public Type(String name) {
        this.name = name;
        fields = new ArrayList<>();
        methods = new ArrayList<>();
    }

    public Type(String packageName, String shortName) {
        this(packageName + "." + shortName);
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
        if (name.contains(".")) {
            return name.substring(0, name.lastIndexOf('.'));
        } else {
            return "";
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        fields.forEach(f -> stringBuilder.append(name).append(" ").append("hasField").append(" ").append(f.toString()
        ).append(" "));
        methods.forEach(m -> stringBuilder.append(name).append(" ").append("hasMethod").append(" ").append(m.toString()
        ).append(" "));
        return stringBuilder.toString();
    }

    public String fullQualifiedName() {
        return name;
    }

    public void addField(Field field) {
        fields.add(field);
    }

    public void addMethod(Method method) {
        methods.add(method);
    }

    public Integer getClusterId() {
        return clusterId;
    }

    public void setClusterId(int clusterId) {
        this.clusterId = clusterId;
    }
}
