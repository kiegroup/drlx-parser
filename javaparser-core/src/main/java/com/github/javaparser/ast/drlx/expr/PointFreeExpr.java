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

package com.github.javaparser.ast.drlx.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;

public class PointFreeExpr extends Expression {

    private final Expression left;
    private final NodeList<Expression> right;

    private final SimpleName operator;
    private boolean negated;
    private final Expression arg1;
    private final Expression arg2;

    public PointFreeExpr( TokenRange tokenRange, Expression left, NodeList<Expression> right, SimpleName operator, Boolean negated, Expression arg1, Expression arg2 ) {
        super(tokenRange);
        this.left = left;
        this.right = right;
        this.operator = operator;
        this.negated = negated;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    @Override
    public <R, A> R accept( GenericVisitor<R, A> v, A arg ) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <A> void accept( VoidVisitor<A> v, A arg ) {
        v.getRuleVisitor().visit( this, arg );
    }

    public Expression getLeft() {
        return left;
    }

    public NodeList<Expression> getRight() {
        return right;
    }

    public SimpleName getOperator() {
        return operator;
    }

    public boolean isNegated() {
        return negated;
    }

    public Expression getArg1() {
        return arg1;
    }

    public Expression getArg2() {
        return arg2;
    }
}
