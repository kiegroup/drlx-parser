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

import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;

public class RulePattern extends Node {

    private final SimpleName type;
    private final SimpleName bind;
    private final OOPathExpr expr;

    public RulePattern( Range range, SimpleName type, SimpleName bind, OOPathExpr expr ) {
        super( range );
        this.type = type;
        this.bind = bind;
        this.expr = expr;
    }

    public SimpleName getType() {
        return type;
    }

    public SimpleName getBind() {
        return bind;
    }

    public OOPathExpr getExpr() {
        return expr;
    }

    @Override
    public <R, A> R accept( GenericVisitor<R, A> v, A arg ) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <A> void accept( VoidVisitor<A> v, A arg ) {
        v.getRuleVisitor().visit( this, arg );
    }
}
