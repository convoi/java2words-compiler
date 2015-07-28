package com.blocksberg.java2word2vec.model;

import java.util.List;

/**
 * @author jh
 */
public class Field {
    private List<String> modifier;
    private Type type;


    public Field(List<String> modifier, Type type) {
        this.modifier = modifier;
        this.type = type;
    }

    @Override
    public String toString() {
        return type.fullQualifiedName();
    }
}
