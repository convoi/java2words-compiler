package com.blocksberg.java2word2vec;

import com.blocksberg.java2word2vec.grammar.JavaBaseListener;
import com.blocksberg.java2word2vec.grammar.JavaParser;

import java.util.List;

/**
 * Collects all full qualified types in the class path.
 *
 * @author jh
 */
public class JavaTypeScanningTypesListener extends JavaBaseListener {
    private final List<String> knownClasses;
    private String packageName;

    public JavaTypeScanningTypesListener(List<String> knownClasses) {
        this.knownClasses = knownClasses;
    }

    @Override
    public void enterPackageDeclaration(JavaParser.PackageDeclarationContext ctx) {
        this.packageName = ctx.qualifiedName().getText();
    }

    @Override
    public void enterClassDeclaration(JavaParser.ClassDeclarationContext ctx) {
        knownClasses.add(packageName + "." + ctx.Identifier().getText());
    }
}
