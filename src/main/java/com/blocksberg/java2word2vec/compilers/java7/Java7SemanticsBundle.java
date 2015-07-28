package com.blocksberg.java2word2vec.compilers.java7;

import com.blocksberg.java2word2vec.compilers.TypeCompiler;

/**
 * @author jh
 */
public class Java7SemanticsBundle extends Java7CompilerBundle {
    @Override
    public int numberOfPasses() {
        return 3;
    }

    @Override
    public TypeCompiler getCompilerInstance(int pass) {
        if (pass == 0) {
            return getTypeLibraryCompiler();
        } else if (pass == 1) {
            return getSemanticsCompiler();
        } else {
            return getTypesRenderer();
        }
    }

    private TypeCompiler getSemanticsCompiler() {
        return new Java7Type2Semantics(knownTypesLibrary);
    }

    private TypeCompiler getTypesRenderer() {
        return new KnownTypesRenderer(knownTypesLibrary);
    }
}
