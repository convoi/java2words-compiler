package com.blocksberg.java2word2vec.compilers.java7;

import com.blocksberg.java2word2vec.compilers.TypeCompiler;
import com.blocksberg.java2word2vec.grammar.JavaBaseListener;
import com.blocksberg.java2word2vec.grammar.JavaParser;
import com.blocksberg.java2word2vec.model.Type;

import java.util.Set;

/**
 * Collects all full qualified types in the class path.
 *
 * @author jh
 */
public class JavaTypeScanner extends JavaBaseListener implements TypeCompiler {
    private final Set<Type> knownTypes;
    private String packageName;

    public JavaTypeScanner(Set<Type> knownTypes) {
        this.knownTypes = knownTypes;
    }

    @Override
    public void enterPackageDeclaration(JavaParser.PackageDeclarationContext ctx) {
        this.packageName = ctx.qualifiedName().getText();
    }

    @Override
    public void enterClassDeclaration(JavaParser.ClassDeclarationContext ctx) {
        knownTypes.add(new Type(packageName + "." + ctx.Identifier().getText()));
    }

    public Set<Type> getKnownTypes() {
        return knownTypes;
    }

    @Override
    public boolean producesOutput() {
        return false;
    }

    @Override
    public String getOutput() {
        return null;
    }
}
