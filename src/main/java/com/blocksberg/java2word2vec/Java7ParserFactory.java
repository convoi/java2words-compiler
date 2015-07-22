package com.blocksberg.java2word2vec;

import com.blocksberg.java2word2vec.grammar.JavaLexer;
import com.blocksberg.java2word2vec.grammar.JavaParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * @author jh
 */
public class Java7ParserFactory implements ParserFactory<JavaParser> {
    @Override
    public ParseTreeListener createTypesListener() {
        return new JavaTypesListener();
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
