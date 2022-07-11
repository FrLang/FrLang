package org.redstom.language.parser.ast.expression;


import lombok.Builder;
import lombok.Data;

@Builder
public @Data class BinaryExpression implements Expression {

    private String operator;
    private Expression left;
    private Expression right;

}
