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

package org.drools.drlx;

import java.util.concurrent.TimeUnit;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.drlx.OOPathExpr;
import com.github.javaparser.ast.drlx.expr.DrlxExpression;
import com.github.javaparser.ast.drlx.expr.PointFreeExpr;
import com.github.javaparser.ast.drlx.expr.TemporalLiteralChunkExpr;
import com.github.javaparser.ast.drlx.expr.TemporalLiteralExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BinaryExpr.Operator;
import com.github.javaparser.ast.expr.Expression;
import org.junit.Test;

import static com.github.javaparser.printer.PrintUtil.toDrlx;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DrlxParserTest {

    @Test
    public void testParseSimpleExpr() {
        String expr = "name == \"Mark\"";
        Expression expression = DrlxParser.parseExpression( expr ).getExpr();
        System.out.println(expression);

        BinaryExpr binaryExpr = ( (BinaryExpr) expression );
        assertEquals("name", binaryExpr.getLeft().toString());
        assertEquals("\"Mark\"", binaryExpr.getRight().toString());
        assertEquals(Operator.EQUALS, binaryExpr.getOperator());
    }

    @Test
    public void testParseSafeCastExpr() {
        String expr = "this instanceof Person && ((Person)this).name == \"Mark\"";
        Expression expression = DrlxParser.parseExpression( expr ).getExpr();
        System.out.println(expression);
    }

    @Test
    public void testParseInlineCastExpr() {
        String expr = "this#Person.name == \"Mark\"";
        Expression expression = DrlxParser.parseExpression( expr ).getExpr();
        assertEquals(expr, toDrlx(expression));
    }

    @Test
    public void testNotAllowedInlineCastInJava() {
        String expr = "this#Person.name == \"Mark\"";
        try {
            Expression expression = JavaParser.parseExpression( expr );
            fail( "Parsing of non valid java expression must fail" );
        } catch (ParseProblemException e) { }
    }

    @Test
    public void testParseNullSafeFieldAccessExpr() {
        String expr = "person!.name == \"Mark\"";
        Expression expression = DrlxParser.parseExpression( expr ).getExpr();
        assertEquals(expr, toDrlx(expression));
    }

    @Test
    public void testDotFreeExpr() {
        String expr = "this after $a";
        Expression expression = DrlxParser.parseExpression( expr ).getExpr();
        assertTrue(expression instanceof PointFreeExpr);
        assertEquals(expr, toDrlx(expression));
    }

    @Test
    public void testDotFreeExprWithArgs() {
        String expr = "this after[5,8] $a";
        Expression expression = DrlxParser.parseExpression( expr ).getExpr();
        assertTrue(expression instanceof PointFreeExpr);
        assertEquals("this after[5s,8s] $a", toDrlx(expression)); // please note the parsed expression once normalized would take the time unit for seconds.
    }

    @Test
    public void testDotFreeExprWithTemporalArgs() {
        String expr = "this after[5ms,8d] $a";
        Expression expression = DrlxParser.parseExpression( expr ).getExpr();
        assertTrue(expression instanceof PointFreeExpr);
        assertEquals(expr, toDrlx(expression));
    }

    @Test
    public void testOOPathExpr() {
        String expr = "/wife/children[age > 10]/toys";
        DrlxExpression drlx = DrlxParser.parseExpression( expr );
        Expression expression = drlx.getExpr();
        assertTrue(expression instanceof OOPathExpr);
        assertEquals(expr, toDrlx(drlx));
    }

    @Test
    public void testOOPathExprWithDeclaration() {
        String expr = "$toy : /wife/children[age > 10]/toys";
        DrlxExpression drlx = DrlxParser.parseExpression( expr );
        assertEquals("$toy", drlx.getBind().asString());
        Expression expression = drlx.getExpr();
        assertTrue(expression instanceof OOPathExpr);
        assertEquals(expr, toDrlx(drlx));
    }

    @Test
    public void testParseTemporalLiteral() {
        String expr = "5s";
        TemporalLiteralExpr drlx = DrlxParser.parseTemporalLiteral(expr);
        assertEquals(expr, toDrlx(drlx));
        assertEquals(1, drlx.getChunks().size());
        TemporalLiteralChunkExpr chunk0 = drlx.getChunks().get(0);
        assertEquals(5, chunk0.getValue());
        assertEquals(TimeUnit.SECONDS, chunk0.getTimeUnit());
    }

    @Test
    public void testParseTemporalLiteralOf2Chunks() {
        String expr = "1m5s";
        TemporalLiteralExpr drlx = DrlxParser.parseTemporalLiteral(expr);
        assertEquals(expr, toDrlx(drlx));
        assertEquals(2, drlx.getChunks().size());
        TemporalLiteralChunkExpr chunk0 = drlx.getChunks().get(0);
        assertEquals(1, chunk0.getValue());
        assertEquals(TimeUnit.MINUTES, chunk0.getTimeUnit());
        TemporalLiteralChunkExpr chunk1 = drlx.getChunks().get(1);
        assertEquals(5, chunk1.getValue());
        assertEquals(TimeUnit.SECONDS, chunk1.getTimeUnit());
    }
}
