package com.blocksberg.java2word2vec.compilers.java7;

import com.blocksberg.java2word2vec.compilers.TypeCompiler;
import com.blocksberg.java2word2vec.grammar.JavaBaseListener;
import com.blocksberg.java2word2vec.grammar.JavaParser;
import com.blocksberg.java2word2vec.model.Type;

/**
 * Collects all full qualified types in the class path.
 *
 * @author jh
 */
public class JavaTypeScanner extends JavaBaseListener implements TypeCompiler {
    private final KnownTypesLibrary knownTypesLibrary;
    private String packageName;

    public JavaTypeScanner(KnownTypesLibrary knownTypesLibrary) {
        this.knownTypesLibrary = knownTypesLibrary;
    }

    @Override
    public void enterPackageDeclaration(JavaParser.PackageDeclarationContext ctx) {
        this.packageName = ctx.qualifiedName().getText();
    }

    @Override
    public void enterClassDeclaration(JavaParser.ClassDeclarationContext ctx) {
        final String shortName = ctx.Identifier().getText();
        addClassOrInterface(shortName);
    }

    @Override
    public void enterInterfaceDeclaration(JavaParser.InterfaceDeclarationContext ctx) {
        String shortName = ctx.Identifier().getText();
        addClassOrInterface(shortName);
    }

    @Override
    public void enterEnumDeclaration(JavaParser.EnumDeclarationContext ctx) {
        String shortName = ctx.Identifier().getText();
        addClassOrInterface(shortName);
    }

    @Override
    public void enterAnnotationTypeDeclaration(JavaParser.AnnotationTypeDeclarationContext ctx) {
        String shortName = ctx.Identifier().getText();
        addClassOrInterface(shortName);
    }

    private void addClassOrInterface(String shortName) {
        knownTypesLibrary.addType(new Type(packageName + "." + shortName));
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
