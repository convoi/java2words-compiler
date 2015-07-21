package com.blocksberg.java2word2vec;

import com.blocksberg.java2word2vec.grammar.JavaBaseListener;
import com.blocksberg.java2word2vec.grammar.JavaParser;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jh
 */
public class JavaTypesListener extends JavaBaseListener {
    private final StringBuilder stringBuilder;
    private String packageName;
    private Map<String, String> imports;

    public JavaTypesListener() {
        stringBuilder = new StringBuilder();
        imports = new HashMap<>();
    }

    @Override
    public void enterImportDeclaration(JavaParser.ImportDeclarationContext ctx) {
        final String fullQualifiedName = ctx.qualifiedName().getText();
        final String shortName = fullQualifiedName.substring(fullQualifiedName.lastIndexOf('.') + 1);
        imports.put(shortName, fullQualifiedName);
    }

    @Override
    public void enterPackageDeclaration(JavaParser.PackageDeclarationContext ctx) {
        this.packageName = ctx.qualifiedName().getText();
    }

    @Override
    public void enterClassDeclaration(JavaParser.ClassDeclarationContext ctx) {
        appendType(ctx.Identifier().getText());
    }

    @Override
    public void enterClassOrInterfaceType(JavaParser.ClassOrInterfaceTypeContext ctx) {
        appendType(ctx.getText());
    }

    @Override
    public void enterPrimitiveType(JavaParser.PrimitiveTypeContext ctx) {
        appendPrimitiveType(ctx.getText());
    }

    private void appendPrimitiveType(String primitiveType) {
        stringBuilder.append(primitiveType).append(" ");
    }

    private void appendType(String shortName) {
        final String fullQualifiedName;
        if (imports.containsKey(shortName)) {
            fullQualifiedName = imports.get(shortName);
        } else if (JavaLangClasses.JAVA_LANG_TYPES.containsKey(shortName)) {
            fullQualifiedName = JavaLangClasses.JAVA_LANG_TYPES.get(shortName);
        } else {
            fullQualifiedName = packageName + "." + shortName;
        }
        stringBuilder.append(fullQualifiedName).append(" ");


    }

    public StringBuilder getStringBuilder() {
        return stringBuilder;
    }
}
