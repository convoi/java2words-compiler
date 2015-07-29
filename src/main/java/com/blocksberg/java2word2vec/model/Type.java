package com.blocksberg.java2word2vec.model;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author jh
 */
public class Type {
    private static final String METHODS = "methods";
    private static final String FIELDS = "fields";
    private Multiset<Type> dependsOn;
    private String name;
    private List<Method> methods;
    private List<Field> fields;
    private Integer clusterId;
    private Type extendsType;
    private Set<Type> annotatedBy;

    public Type(String name) {
        this.name = name;
        fields = new ArrayList<>();
        methods = new ArrayList<>();
        annotatedBy = new HashSet<>();
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
        annotatedBy.forEach(a -> stringBuilder.append(name).append(" ").append("isAnnotatedBy").append(" ")
                .append(a.fullQualifiedName()).append(" "));
        if (extendsType != null) {
            stringBuilder.append(name).append(" ").append("extends").append(" ").append(extendsType.fullQualifiedName
                    ()).append(" ");
        }
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

    public Map<String, Number> getStatistics() {
        final HashMap<String, Number> statistics = new HashMap<>();
        statistics.put(METHODS, methods.size());
        statistics.put(FIELDS, fields.size());
        return statistics;
    }

    public void addAnnotation(Type type) {
        annotatedBy.add(type);
    }

    public Set<Type> getAnnotatedBy() {
        return annotatedBy;
    }

    public void setExtendsType(Type extendsType) {
        this.extendsType = extendsType;
    }
}
