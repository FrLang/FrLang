package org.redstom.language.parser.ast.expression;

import org.redstom.language.parser.rdp.ParseContext;
import org.redstom.language.parser.rdp.ParserElement;

public class LeftHandSide {

    /**
     * LeftHandSideExpression
     * : PrimaryExpression
     * ;
     */
    public static class Parser implements ParserElement<Expression, Void> {
        @Override
        public Expression parse(ParseContext ctx, Void unused) {
            return ctx.parse(Expression.PrimaryParser.class);
        }
    }
}
