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

import java.util.function.Function;
import java.util.function.Predicate;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.drlx.RuleDeclaration;
import com.github.javaparser.ast.visitor.VoidRuleVisitor;

public class PrintUtil {

    public static String toJava(Node node ) {
        return node.toString();
    }

    public static String toDrlx(Node node) {
        return node.toString(getConf( DrlxPrintVisitor::new, type -> true ));
    }

    public static String toDrl(Node node) {
        return node.toString(getConf( DrlPrintVisitor::new, type -> type instanceof RuleDeclaration ) );
    }

    private static PrettyPrinterConfiguration getConf( Function<PrettyPrintVisitor, VoidRuleVisitor<Void>> f, Predicate<TypeDeclaration> p ) {
        return new PrettyPrinterConfiguration().setVisitorFactory( c -> {
            PrettyPrintVisitor visitor = new PrettyPrintVisitor( c );
            visitor.setRuleVisitor( f.apply( visitor ) );
            return visitor;
        } );
    }
}
