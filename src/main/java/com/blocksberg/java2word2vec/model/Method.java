package com.blocksberg.java2word2vec.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jh
 */
public class Method {
    private String name;
    private Type result;
    private List<String> modifier;
    private List<Type> parameter;

    public Method(String name, Type result, List<Type> parameter) {
        this.name = name;
        this.parameter = parameter;
        this.result = result;
        modifier = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder =
                new StringBuilder("thatReturns " + (result == null ? "void" : result.fullQualifiedName())
                        + " ");
        if (!parameter.isEmpty()) {
            stringBuilder.append("by ");
            parameter.forEach(t -> stringBuilder.append(t.fullQualifiedName()).append(" "));
        }
        return stringBuilder.toString().trim();
    }
}
