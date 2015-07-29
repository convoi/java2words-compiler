package com.blocksberg.java2word2vec.compilers.java7;

import com.blocksberg.java2word2vec.compilers.JavaLangClasses;
import com.blocksberg.java2word2vec.compilers.PrimitiveTypes;
import com.blocksberg.java2word2vec.compilers.TypeCompiler;
import com.blocksberg.java2word2vec.grammar.JavaBaseListener;
import com.blocksberg.java2word2vec.grammar.JavaParser;
import com.blocksberg.java2word2vec.model.Field;
import com.blocksberg.java2word2vec.model.Method;
import com.blocksberg.java2word2vec.model.Type;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Compiles Java code to a stream of words (i.e. the Types used).
 *
 * @author jh
 */
public class Java7Type2Semantics extends JavaBaseListener implements TypeCompiler {
    private final KnownTypesLibrary knownTypesLibrary;
    private String packageName;
    private Deque<Type> typeStack;
    private final KnownTypesLibrary imports;
    private List<String> starImports;

    private boolean addClassToAllMethods;

    public Java7Type2Semantics(KnownTypesLibrary knownTypesLibrary) {
        this.knownTypesLibrary = knownTypesLibrary;
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
        final Type currentType = typeStack.peek();
        Type fieldType = makeType(ctx.type());
        /*final JavaParser.ClassBodyDeclarationContext parent =
                (JavaParser.ClassBodyDeclarationContext) ctx.getParent().getParent();*/
        //TODO modifier
        currentType.addField(new Field(null, fieldType));
    }

    private Type makeType(JavaParser.TypeContext ctx) {
        if (ctx.classOrInterfaceType() != null) {
            final String shortName = ctx.classOrInterfaceType().Identifier(0).getText();
            return resolveType(shortName);
            /*final List<JavaParser.TypeArgumentsContext> typeArgumentsContexts =
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
            }*/
        } else if (ctx.primitiveType() != null) {
            return resolveType(ctx.primitiveType().getText());
        } else {
            return null;
        }
    }

    @Override
    public void enterMethodDeclaration(JavaParser.MethodDeclarationContext ctx) {
        final Type currentType = typeStack.peek();
        Method method = createMethod(ctx);
        currentType.addMethod(method);
    }

    private Method createMethod(JavaParser.MethodDeclarationContext ctx) {
        final List<Type> parameters;
        if (ctx.formalParameters() != null && ctx.formalParameters().formalParameterList() != null) {
            final JavaParser.FormalParameterListContext formalParameterListContext =
                    ctx.formalParameters().formalParameterList();

            parameters =
                    formalParameterListContext.formalParameter().stream().map(p -> makeType(p.type())).collect(
                            Collectors.toList());
        } else {
            parameters = Collections.emptyList();
        }
        final Type result;
        if (ctx.type() != null) {
            result = makeType(ctx.type());
        } else {
            result = null;
        }
        return new Method(ctx.Identifier().toString(), result, parameters);
    }


    @Override
    public void enterPackageDeclaration(JavaParser.PackageDeclarationContext ctx) {
        this.packageName = ctx.qualifiedName().getText();
    }

    @Override
    public void enterClassDeclaration(JavaParser.ClassDeclarationContext ctx) {
        List<String> annotations = getAnnotationsFromDeclaration(ctx.getParent());
        handleClassOrInterfaceDeclaration(ctx.Identifier().getText(), annotations);
    }

    private List<String> getAnnotationsFromDeclaration(ParserRuleContext typeDeclarationContext) {
        List<JavaParser.ClassOrInterfaceModifierContext> modifierContext = null;
        if (typeDeclarationContext instanceof JavaParser.TypeDeclarationContext) {
            modifierContext = ((JavaParser.TypeDeclarationContext) typeDeclarationContext).classOrInterfaceModifier();
        }
        List<String> annotations = Collections.emptyList();
        if (modifierContext != null) {
            annotations = modifierContext.stream()
                    .filter(m -> m.annotation() != null)
                    .map(a -> a.annotation().annotationName().getText()).collect(Collectors.toList());
        }
        return annotations;
    }

    @Override
    public void enterInterfaceDeclaration(JavaParser.InterfaceDeclarationContext ctx) {
        List<String> annotations = getAnnotationsFromDeclaration(ctx.getParent());
        handleClassOrInterfaceDeclaration(ctx.Identifier().getText(), annotations);
    }

    @Override
    public void enterEnumDeclaration(JavaParser.EnumDeclarationContext ctx) {
        List<String> annotations = getAnnotationsFromDeclaration(ctx.getParent());
        handleClassOrInterfaceDeclaration(ctx.Identifier().getText(), annotations);
    }

    private void handleClassOrInterfaceDeclaration(String shortName, List<String> annotations) {
        final Type type = resolveType(shortName);
        annotations.forEach(a -> type.addAnnotation(resolveType(a)));
        typeStack.add(type);
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


    private Type resolveType(String shortName) {
        if (imports.knows(shortName)) {
            return new Type(imports.findFirst(shortName).get().fullQualifiedName());
        } else if (PrimitiveTypes.PRIMITIVE_TYPES.containsKey(shortName)) {
            return PrimitiveTypes.PRIMITIVE_TYPES.get(shortName);
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
        return false;
    }

    @Override
    public String getOutput() {
        return null;
    }
}
