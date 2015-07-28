package com.blocksberg.java2word2vec.compilers.java7;

import com.blocksberg.java2word2vec.compilers.CompilerBundle;
import com.blocksberg.java2word2vec.compilers.TypeCompiler;
import com.blocksberg.java2word2vec.grammar.JavaLexer;
import com.blocksberg.java2word2vec.grammar.JavaParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * @author jh
 */
public class Java7CompilerBundle implements CompilerBundle<JavaParser> {

    private JavaTypeScanner typeLibraryCompiler;
    protected final KnownTypesLibrary knownTypesLibrary;

    public Java7CompilerBundle() {
        this.knownTypesLibrary = new KnownTypesLibrary();
    }


    @Override
    public int numberOfPasses() {
        return 2;
    }

    @Override
    public TypeCompiler getCompilerInstance(int pass) {
        if (pass == 0) {
            return getTypeLibraryCompiler();
        } else {
            return new Java7Type2WordVecCompiler(knownTypesLibrary);
        }
    }

    protected TypeCompiler getTypeLibraryCompiler() {
        if (typeLibraryCompiler == null) {
            typeLibraryCompiler = new JavaTypeScanner(knownTypesLibrary);
        }
        return typeLibraryCompiler;
    }

    @Override
    public Lexer createLexer(CharStream input) {
        return new JavaLexer(input);
    }

    @Override
    public JavaParser createParser(TokenStream tokenStream) {
        return new JavaParser(tokenStream);
    }

    @Override
    public ParseTree rootElement(JavaParser parser) {
        return parser.compilationUnit();
    }
}
