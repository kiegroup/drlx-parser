package com.github.javaparser.generator.core.node;

import java.util.Arrays;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.generator.NodeGenerator;
import com.github.javaparser.metamodel.BaseNodeMetaModel;
import com.github.javaparser.utils.SourceRoot;

public class FinalGenerator extends NodeGenerator {
    public FinalGenerator(SourceRoot sourceRoot) {
        super(sourceRoot);
    }

    @Override
    protected void generateNode(BaseNodeMetaModel nodeMetaModel, CompilationUnit nodeCu, ClassOrInterfaceDeclaration nodeCoid) {
        if(!Arrays.asList("CastExpr", "MethodCallExpr", "FieldAccessExpr").contains(nodeCoid.getName().asString()))
            nodeCoid.setFinal(!nodeMetaModel.isAbstract());
    }
}
