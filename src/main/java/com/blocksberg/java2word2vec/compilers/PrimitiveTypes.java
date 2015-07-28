package com.blocksberg.java2word2vec.compilers;

import com.blocksberg.java2word2vec.model.Type;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jh
 */
public class PrimitiveTypes {
    public final static Map<String, Type> PRIMITIVE_TYPES = new HashMap<>();

    static {
        Arrays.asList("int", "float", "double", "boolean", "short", "long", "char", "byte").forEach(p ->
                PRIMITIVE_TYPES.put(p, new Type(p)));
    }
}
