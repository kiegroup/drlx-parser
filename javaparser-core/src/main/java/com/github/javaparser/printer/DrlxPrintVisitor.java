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

package com.github.javaparser.printer;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.drlx.OOPathChunk;
import com.github.javaparser.ast.drlx.OOPathExpr;
import com.github.javaparser.ast.drlx.RuleBody;
import com.github.javaparser.ast.drlx.RuleDeclaration;
import com.github.javaparser.ast.drlx.expr.DrlxExpression;
import com.github.javaparser.ast.drlx.expr.InlineCastExpr;
import com.github.javaparser.ast.drlx.expr.NullSafeFieldAccessExpr;
import com.github.javaparser.ast.drlx.expr.NullSafeMethodCallExpr;
import com.github.javaparser.ast.drlx.expr.PointFreeExpr;
import com.github.javaparser.ast.drlx.expr.TemporalLiteralChunkExpr;
import com.github.javaparser.ast.drlx.expr.TemporalLiteralExpr;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.visitor.AbstractVoidRuleVisitor;

public class DrlxPrintVisitor extends AbstractVoidRuleVisitor<Void, PrettyPrintVisitor> {

    public DrlxPrintVisitor(PrettyPrintVisitor visitor) {
        super( visitor );
    }

    @Override
    public void visit( RuleDeclaration n, Void arg ) {
        visitor.printJavaComment(n.getComment(), arg);

        for (AnnotationExpr ae : n.getAnnotations()) {
            ae.accept(visitor, arg);
            visitor.printer.print(" ");
        }

        visitor.printer.print("rule ");
        n.getName().accept(visitor, arg);
        visitor.printer.println(" {");
        n.getRuleBody().accept(visitor, arg);
        visitor.printer.println("}");
    }

    @Override
    public void visit( RuleBody ruleBody, Void arg ) {
    }

    @Override
    public void visit( InlineCastExpr inlineCastExpr, Void arg ) {
        visitor.printJavaComment(inlineCastExpr.getComment(), arg);
        inlineCastExpr.getExpression().accept( visitor, arg );
        visitor.printer.print( "#" );
        inlineCastExpr.getType().accept( visitor, arg );
    }

    @Override
    public void visit( NullSafeFieldAccessExpr nullSafeFieldAccessExpr, Void arg ) {
        visitor.printJavaComment(nullSafeFieldAccessExpr.getComment(), arg);
        nullSafeFieldAccessExpr.getScope().accept( visitor, arg );
        visitor.printer.print( "!." );
        nullSafeFieldAccessExpr.getName().accept( visitor, arg );
    }

    @Override
    public void visit( NullSafeMethodCallExpr nullSafeMethodCallExpr, Void arg ) {
        visitor.printJavaComment(nullSafeMethodCallExpr.getComment(), arg);
        if (nullSafeMethodCallExpr.getScope().isPresent()) {
            nullSafeMethodCallExpr.getScope().get().accept( visitor, arg );
            visitor.printer.print("!.");
        }
        visitor.printTypeArgs(nullSafeMethodCallExpr, arg);
        nullSafeMethodCallExpr.getName().accept( visitor, arg );
        visitor.printArguments(nullSafeMethodCallExpr.getArguments(), arg);
    }

    @Override
    public void visit( PointFreeExpr pointFreeExpr, Void arg ) {
        visitor.printJavaComment(pointFreeExpr.getComment(), arg);
        pointFreeExpr.getLeft().accept( visitor, arg );
        visitor.printer.print(" ");
        pointFreeExpr.getOperator().accept( visitor, arg );
        if (pointFreeExpr.getArg1() != null) {
            visitor.printer.print("[");
            pointFreeExpr.getArg1().accept( visitor, arg );
            if (pointFreeExpr.getArg2() != null) {
                visitor.printer.print(",");
                pointFreeExpr.getArg2().accept( visitor, arg );
            }
            visitor.printer.print("]");
        }
        visitor.printer.print(" ");
        NodeList<Expression> rightExprs = pointFreeExpr.getRight();
        if (rightExprs.size() == 1) {
            rightExprs.get(0).accept( visitor, arg );
        } else {
            visitor.printer.print("(");
            rightExprs.get(0).accept( visitor, arg );
            for (int i = 1; i < rightExprs.size(); i++) {
                visitor.printer.print(", ");
                rightExprs.get(i).accept( visitor, arg );
            }
            visitor.printer.print(")");
        }
    }

    @Override
    public void visit(TemporalLiteralExpr temporalLiteralExpr, Void arg) {
        visitor.printJavaComment(temporalLiteralExpr.getComment(), arg);
        NodeList<TemporalLiteralChunkExpr> chunks = temporalLiteralExpr.getChunks();
        for (TemporalLiteralChunkExpr c : chunks) {
            c.accept(visitor, arg);
        }
    }

    @Override
    public void visit(TemporalLiteralChunkExpr temporalLiteralExpr, Void arg) {
        visitor.printJavaComment(temporalLiteralExpr.getComment(), arg);
        visitor.printer.print("" + temporalLiteralExpr.getValue());
        switch (temporalLiteralExpr.getTimeUnit()) {
            case MILLISECONDS:
                visitor.printer.print("ms");
                break;
            case SECONDS:
                visitor.printer.print("s");
                break;
            case MINUTES:
                visitor.printer.print("m");
                break;
            case HOURS:
                visitor.printer.print("h");
                break;
            case DAYS:
                visitor.printer.print("d");
                break;
        }
    }

    @Override
    public void visit( DrlxExpression expr, Void arg ) {
        if (expr.getBind() != null) {
            expr.getBind().accept( visitor, arg );
            visitor.printer.print( " : " );
        }
        expr.getExpr().accept(visitor, arg);
    }

    @Override
    public void visit(OOPathExpr pointFreeExpr, Void arg ) {
        visitor.printJavaComment(pointFreeExpr.getComment(), arg);
        visitor.printer.print("/");
        NodeList<OOPathChunk> chunks = pointFreeExpr.getChunks();
        for (int i = 0; i <  chunks.size(); i++) {
            OOPathChunk o = chunks.get(i);
            visitor.printer.print(o.getField().toString());
            Expression condition = o.getCondition();
            if (condition != null) {
                visitor.printer.print("[");
                visitor.printer.print(condition.toString());
                visitor.printer.print("]");
            }
            if(i != chunks.size() - 1) { // Avoid printing last /
                visitor.printer.print("/");
            }
        }
    }
}
