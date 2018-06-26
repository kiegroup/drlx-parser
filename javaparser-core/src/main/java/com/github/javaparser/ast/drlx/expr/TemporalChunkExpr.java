package com.github.javaparser.ast.drlx.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.expr.LiteralExpr;

public abstract class TemporalChunkExpr extends LiteralExpr {

    public TemporalChunkExpr() {
    }

    public TemporalChunkExpr(TokenRange tokenRange) {
        super(tokenRange);
    }
}
