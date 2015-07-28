package com.blocksberg.java2word2vec.compilers;

import com.blocksberg.java2word2vec.compilers.java7.KnownTypesLibrary;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Bundles all necessary parts to compile a file.
 *
 * @author jh
 */
public interface CompilerBundle<T extends Parser> {

    int numberOfPasses();

    TypeCompiler getCompilerInstance(int pass);

    Lexer createLexer(CharStream input);

    T createParser(TokenStream tokenStream);

    ParseTree rootElement(T parser);

    KnownTypesLibrary getKnownTypesLibrary();
}
