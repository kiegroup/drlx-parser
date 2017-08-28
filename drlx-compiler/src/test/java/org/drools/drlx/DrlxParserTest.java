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

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BinaryExpr.Operator;
import com.github.javaparser.ast.expr.Expression;
import org.junit.Test;

import static com.github.javaparser.printer.PrintUtil.toDrlx;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DrlxParserTest {

    @Test
    public void testParseSimpleExpr() {
        String expr = "name == \"Mark\"";
        Expression expression = DrlxParser.parseExpression( expr );
        System.out.println(expression);

        BinaryExpr binaryExpr = ( (BinaryExpr) expression );
        assertEquals("name", binaryExpr.getLeft().toString());
        assertEquals("\"Mark\"", binaryExpr.getRight().toString());
        assertEquals(Operator.EQUALS, binaryExpr.getOperator());
    }

    @Test
    public void testParseSafeCastExpr() {
        String expr = "this instanceof Person && ((Person)this).name == \"Mark\"";
        Expression expression = DrlxParser.parseExpression( expr );
        System.out.println(expression);
    }

    @Test
    public void testParseInlineCastExpr() {
        String expr = "this#Person.name == \"Mark\"";
        Expression expression = DrlxParser.parseExpression( expr );
        System.out.println(toDrlx(expression));
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
        Expression expression = DrlxParser.parseExpression( expr );
        System.out.println(toDrlx(expression));
    }
}
