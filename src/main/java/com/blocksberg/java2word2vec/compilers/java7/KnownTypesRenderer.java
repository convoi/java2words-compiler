package com.blocksberg.java2word2vec.compilers.java7;

import com.blocksberg.java2word2vec.compilers.TypeCompiler;
import com.blocksberg.java2word2vec.grammar.JavaBaseListener;
import com.blocksberg.java2word2vec.grammar.JavaParser;
import com.blocksberg.java2word2vec.model.Type;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.Optional;

/**
 * @author jh
 */
public class KnownTypesRenderer extends JavaBaseListener implements TypeCompiler {

    private String packageName;
    private Deque<Type> typeStack;
    private final KnownTypesLibrary knownTypesLibrary;
    private StringBuilder stringBuilder;

    public KnownTypesRenderer(KnownTypesLibrary knownTypesLibrary) {
        this.knownTypesLibrary = knownTypesLibrary;
        typeStack = new ArrayDeque<>();
        stringBuilder = new StringBuilder();
    }

    @Override
    public void enterPackageDeclaration(JavaParser.PackageDeclarationContext ctx) {
        this.packageName = ctx.qualifiedName().getText();
    }

    @Override
    public void enterClassDeclaration(JavaParser.ClassDeclarationContext ctx) {
        handleClassOrInterfaceDeclaration(ctx.Identifier().getText());
    }

    @Override
    public void enterInterfaceDeclaration(JavaParser.InterfaceDeclarationContext ctx) {
        handleClassOrInterfaceDeclaration(ctx.Identifier().getText());
    }

    @Override
    public void enterEnumDeclaration(JavaParser.EnumDeclarationContext ctx) {
        handleClassOrInterfaceDeclaration(ctx.Identifier().getText());
    }

    private void handleClassOrInterfaceDeclaration(String shortName) {
        final Type type = resolveType(shortName);
        stringBuilder.append(type.toString());
    }

    private Type resolveType(String shortName) {
        final Optional<Type>
                typeInSamePackage = knownTypesLibrary.findTypeInPackages(Collections.singleton(packageName),
                shortName);
        if(!typeInSamePackage.isPresent()) {
            return new Type(shortName);
        }
        return typeInSamePackage.get();
    }

    @Override
    public boolean producesOutput() {
        return true;
    }

    @Override
    public String getOutput() {
        return stringBuilder.toString();
    }
}
