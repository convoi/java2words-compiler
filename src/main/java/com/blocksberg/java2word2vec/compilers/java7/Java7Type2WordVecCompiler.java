package com.blocksberg.java2word2vec.compilers.java7;

import com.blocksberg.java2word2vec.compilers.JavaLangClasses;
import com.blocksberg.java2word2vec.compilers.TypeCompiler;
import com.blocksberg.java2word2vec.grammar.JavaBaseListener;
import com.blocksberg.java2word2vec.grammar.JavaParser;
import com.blocksberg.java2word2vec.model.Type;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

/**
 * Compiles Java code to a stream of words (i.e. the Types used).
 *
 * @author jh
 */
public class Java7Type2WordVecCompiler extends JavaBaseListener implements TypeCompiler {
    private final StringBuilder stringBuilder;
    private final KnownTypesLibrary knownTypesLibrary;
    private String packageName;
    private Deque<Type> typeStack;
    private final KnownTypesLibrary imports;
    private List<String> starImports;

    private boolean addClassToAllMethods;

    public Java7Type2WordVecCompiler(KnownTypesLibrary knownTypesLibrary) {
        this.knownTypesLibrary = knownTypesLibrary;
        stringBuilder = new StringBuilder();
        imports = new KnownTypesLibrary();
        starImports = new ArrayList<>();
        typeStack = new ArrayDeque<>();
    }

    @Override
    public void enterImportDeclaration(JavaParser.ImportDeclarationContext ctx) {
        final String fullQualifiedName = ctx.qualifiedName().getText();
        final Type type = new Type(fullQualifiedName);
        if (type.isStarType()) {
            starImports.add(type.packageName());
        } else {
            imports.addType(type);
        }
    }

    @Override
    public void enterFieldDeclaration(JavaParser.FieldDeclarationContext ctx) {
        appendTypeLike(ctx.type());
    }

    @Override
    public void enterMethodDeclaration(JavaParser.MethodDeclarationContext ctx) {
        if (ctx.type() != null) {
            appendCurrentType();
            appendTypeLike(ctx.type());
            if (ctx.formalParameters() != null && ctx.formalParameters().formalParameterList() != null) {
                final JavaParser.FormalParameterListContext formalParameterListContext =
                        ctx.formalParameters().formalParameterList();

                formalParameterListContext.formalParameter().stream().forEach(p -> appendTypeLike(p
                        .type()));
            }
        }
    }

    private void appendCurrentType() {
        appendType(typeStack.peek());
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
        typeStack.add(type);
        appendType(type);
    }

    /*
    we are not listening for types themselves, but rather act on them differently however their context is.
    @Override
    public void enterClassOrInterfaceType(JavaParser.ClassOrInterfaceTypeContext ctx) {
        final String shortName = ctx.Identifier(0).getText();
        appendType(shortName);
    }

    @Override
    public void enterPrimitiveType(JavaParser.PrimitiveTypeContext ctx) {
        appendPrimitiveType(ctx.getText());
    }*/

    private void appendTypeLike(JavaParser.TypeContext ctx) {
        if (ctx.classOrInterfaceType() != null) {
            final String shortName = ctx.classOrInterfaceType().Identifier(0).getText();
            appendType(shortName);
            final List<JavaParser.TypeArgumentsContext> typeArgumentsContexts =
                    ctx.classOrInterfaceType().typeArguments();
            if (typeArgumentsContexts != null) {
                typeArgumentsContexts.forEach(t -> {

                    if (t != null) {
                        t.typeArgument().forEach(x -> {
                            if (x.type() != null) {
                                appendTypeLike(x.type());
                            }
                        });
                    }
                });
            }
        } else if (ctx.primitiveType() != null) {
            appendPrimitiveType(ctx.primitiveType().getText());
        }
    }

    private void appendPrimitiveType(String primitiveType) {
        stringBuilder.append(primitiveType).append(" ");
    }

    private void appendType(String shortName) {
        final Type type = resolveType(shortName);
        appendType(type);
    }

    private void appendType(Type type) {
        stringBuilder.append(type.fullQualifiedName()).append(" ");
    }

    private Type resolveType(String shortName) {
        if (imports.knows(shortName)) {
            return new Type(imports.findFirst(shortName).get().fullQualifiedName());
        } else if (JavaLangClasses.JAVA_LANG_TYPES.containsKey(shortName)) {
            return new Type(JavaLangClasses.JAVA_LANG_TYPES.get(shortName));
        } else {

            final Optional<Type>
                    typeInSamePackage = knownTypesLibrary.findTypeInPackages(Collections.singleton(packageName),
                    shortName);
            return typeInSamePackage.orElse(starImportType(shortName));
        }
    }

    private Type starImportType(String shortName) {
        if (starImports.isEmpty()) {
            return new Type(shortName);
        } else {
            return new Type(starImports.get(0), shortName);
        }
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
