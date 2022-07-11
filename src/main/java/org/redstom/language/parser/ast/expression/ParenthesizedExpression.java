package org.redstom.language.parser.ast.expression;

import org.redstom.language.lexer.tokens.par.CLOSE_PAR;
import org.redstom.language.lexer.tokens.par.OPEN_PAR;
import org.redstom.language.parser.rdp.ParseContext;
import org.redstom.language.parser.rdp.ParserElement;

public class ParenthesizedExpression {

    /**
     * ParenthesizedExpression
     * : '(' Expression ')'
     * ;
     */
    public static class Parser implements ParserElement<Expression, Void> {
        @Override
        public Expression parse(ParseContext ctx, Void unused) {
            ctx.eat(OPEN_PAR.class);
            Expression ex = ctx.parse(Expression.Parser.class);
            ctx.eat(CLOSE_PAR.class);
            return ex;
        }
    }
}
