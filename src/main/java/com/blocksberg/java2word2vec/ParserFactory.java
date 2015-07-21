package com.blocksberg.java2word2vec;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * @author jh
 */
public interface ParserFactory<T extends Parser> {

    Lexer createLexer(CharStream input);

    T createParser(TokenStream tokenStream);

    ParseTree rootElement(T parser);
}
