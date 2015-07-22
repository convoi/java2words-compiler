package com.blocksberg.java2word2vec;

import com.blocksberg.java2word2vec.grammar.Java8Lexer;
import com.blocksberg.java2word2vec.grammar.Java8Parser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * @author jh
 */
public class Java8ParserFactory implements ParserFactory<Java8Parser> {
    @Override
    public ParseTreeListener createTypesListener() {
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
}
