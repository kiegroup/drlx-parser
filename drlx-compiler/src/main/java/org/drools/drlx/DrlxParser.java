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
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParseStart;
import com.github.javaparser.Provider;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;

import static com.github.javaparser.Providers.provider;

public class DrlxParser {

    private static final ParseStart<Expression> DRLX_EXPRESSION = parser -> {
        parser.setDrlParsing(true);
        try {
            return parser.DrlxExpression();
        } finally {
            parser.setDrlParsing(false);
        }
    };

    public static <T extends Expression> T parseExpression(final String expression) {
        return (T) simplifiedParse(DRLX_EXPRESSION, provider(expression));
    }

    private static <T extends Node> T simplifiedParse( ParseStart<T> context, Provider provider ) {
        ParseResult<T> result = new JavaParser().parse(context, provider);
        if (result.isSuccessful()) {
            return result.getResult().get();
        }
        throw new ParseProblemException(result.getProblems());
    }
}
