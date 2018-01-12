/*
 * Copyright 2005 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.javaparser.ast.drlx;

import java.util.EnumSet;

import com.github.javaparser.Range;
import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;

public class RuleDeclaration extends TypeDeclaration<RuleDeclaration> {

    private final RuleBody ruleBody;

    public RuleDeclaration(TokenRange range, NodeList<AnnotationExpr> annotations, SimpleName name, RuleBody ruleBody ) {
        super( range, EnumSet.noneOf(Modifier.class), annotations, name, new NodeList<>() );
        this.ruleBody = ruleBody;
    }

    @Override
    public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
//        return v.visit(this, arg);
        throw new UnsupportedOperationException();
    }

    @Override
    public <A> void accept(final VoidVisitor<A> v, final A arg) {
        v.getRuleVisitor().visit( this, arg );
    }

    public RuleBody getRuleBody() {
        return ruleBody;
    }
}
