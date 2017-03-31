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

package com.github.javaparser.ast.visitor;

import com.github.javaparser.ast.drlx.RuleBody;
import com.github.javaparser.ast.drlx.RuleDeclaration;

public class VoidRuleVisitorAdapter<A> extends AbstractVoidRuleVisitor<A, VoidVisitor<A>> {

    public VoidRuleVisitorAdapter(VoidVisitor<A> visitor) {
        super( visitor );
    }

    @Override
    public void visit( RuleDeclaration n, A arg ) {
        n.getMembers().forEach(p -> p.accept(visitor, arg));
        n.getName().accept(visitor, arg);
        n.getAnnotations().forEach(p -> p.accept(visitor, arg));
        n.getComment().ifPresent(l -> l.accept(visitor, arg));
        n.getRuleBody().accept(visitor, arg);
    }

    @Override
    public void visit( RuleBody n, A arg ) {
        n.getComment().ifPresent(l -> l.accept(visitor, arg));
    }
}
