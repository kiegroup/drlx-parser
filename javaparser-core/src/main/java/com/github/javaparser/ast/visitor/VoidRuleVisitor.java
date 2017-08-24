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

import com.github.javaparser.ast.drlx.OOPathChunk;
import com.github.javaparser.ast.drlx.OOPathExpr;
import com.github.javaparser.ast.drlx.RuleBody;
import com.github.javaparser.ast.drlx.RuleConsequence;
import com.github.javaparser.ast.drlx.RuleDeclaration;
import com.github.javaparser.ast.drlx.RulePattern;
import com.github.javaparser.ast.drlx.expr.InlineCastExpr;

public interface VoidRuleVisitor<A> {

    default void visit( RuleDeclaration ruleDeclaration, A arg ) { }

    default void visit( RuleBody ruleBody, A arg ) { }

    default void visit( RulePattern rulePattern, A arg ) { }

    default void visit( OOPathExpr expr, A arg ) { }

    default void visit( OOPathChunk chunk, A arg ) { }

    default void visit( RuleConsequence ruleConsequence, A arg ) { }

    default void visit( InlineCastExpr inlineCastExpr, A arg ) { }

}
