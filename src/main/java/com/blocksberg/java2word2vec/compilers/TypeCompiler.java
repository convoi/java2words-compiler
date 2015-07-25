package com.blocksberg.java2word2vec.compilers;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * Represents one phase of compilation. Can produce output or not.
 *
 * @author jh
 */
public interface TypeCompiler extends ParseTreeListener {
    boolean producesOutput();

    String getOutput();
}
