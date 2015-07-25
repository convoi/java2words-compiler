package com.blocksberg.java2word2vec.compilers.java7;

import com.blocksberg.java2word2vec.compilers.CompilerBundle;
import com.blocksberg.java2word2vec.compilers.TypeCompiler;
import com.blocksberg.java2word2vec.grammar.JavaLexer;
import com.blocksberg.java2word2vec.grammar.JavaParser;
import com.blocksberg.java2word2vec.model.Type;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.HashSet;
import java.util.Set;

/**
 * @author jh
 */
public class Java7CompilerBundle implements CompilerBundle<JavaParser> {

    private final Set<Type> knownTypes;
    private JavaTypeScanner typeLibraryCompiler;

    public Java7CompilerBundle() {
        this.knownTypes = new HashSet<>();
    }

    public Java7CompilerBundle(Set<Type> knownTypes) {
        this.knownTypes = knownTypes;
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
            return new Java7Type2WordVecCompiler();
        }
    }

    private TypeCompiler getTypeLibraryCompiler() {
        if (typeLibraryCompiler == null) {
            typeLibraryCompiler = new JavaTypeScanner(knownTypes);
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
