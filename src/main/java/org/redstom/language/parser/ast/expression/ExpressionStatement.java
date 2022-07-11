package org.redstom.language.parser.ast.expression;

import lombok.Builder;
import lombok.Data;
import org.redstom.language.lexer.tokens.syntax.SEMICOLON;
import org.redstom.language.parser.ast.statement.Statement;
import org.redstom.language.parser.rdp.ParseContext;
import org.redstom.language.parser.rdp.ParserElement;

@Builder
public @Data class ExpressionStatement implements Statement {

    private Expression expression;

    /**
     * ExpressionStatement
     *  : Expression ';'
     */
    public static class Parser implements ParserElement<ExpressionStatement, Void> {
        @Override
        public ExpressionStatement parse(ParseContext ctx, Void unused) {
            Expression expression = ctx.parse(Expression.Parser.class);
            ctx.eat(SEMICOLON.class);

            return ExpressionStatement.builder()
                    .expression(expression)
                    .build();
        }
    }
}
