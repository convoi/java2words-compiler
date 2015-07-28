package com.blocksberg.java2word2vec.compilers.java8;

import com.blocksberg.java2word2vec.compilers.CompilerBundle;
import com.blocksberg.java2word2vec.compilers.TypeCompiler;
import com.blocksberg.java2word2vec.compilers.java7.KnownTypesLibrary;
import com.blocksberg.java2word2vec.grammar.Java8Lexer;
import com.blocksberg.java2word2vec.grammar.Java8Parser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * @author jh
 */
public class Java8CompilerBundle implements CompilerBundle<Java8Parser> {
    @Override
    public int numberOfPasses() {
        return 1;
    }

    @Override
    public TypeCompiler getCompilerInstance(int pass) {
        return new Java8TypesListener();
    }

    @Override
    public Lexer createLexer(CharStream input) {
        return new Java8Lexer(input);
    }

    @Override
    public Java8Parser createParser(TokenStream tokenStream) {
        return new Java8Parser(tokenStream);
    }

    @Override
    public ParseTree rootElement(Java8Parser parser) {
        return parser.compilationUnit();
    }

    @Override
    public KnownTypesLibrary getKnownTypesLibrary() {
        // TODO implement
        return null;
    }
}
