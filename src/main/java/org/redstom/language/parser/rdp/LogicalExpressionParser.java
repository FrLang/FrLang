package org.redstom.language.parser.rdp;

import org.redstom.language.lexer.tokens.Token;
import org.redstom.language.lexer.tokens.keywords.AND;
import org.redstom.language.lexer.tokens.keywords.OR;
import org.redstom.language.parser.ast.expression.Expression;
import org.redstom.language.parser.ast.expression.LogicalExpression;

import java.util.function.Supplier;

public interface LogicalExpressionParser {
    default Expression logicalExpression(ParseContext ctx,
                                         Supplier<Expression> sup,
                                         Class<? extends Token<String>> clazz) {
        Expression left = sup.get();

        while(clazz.isInstance(ctx.lookahead())) {
            Token<String> operator = ctx.eat(clazz);

            Expression right = sup.get();
            left = LogicalExpression.builder()
                    .operator(operator.value())
                    .left(left)
                    .right(right)
                    .build();
        }

        return left;
    }

    class OrParser implements ParserElement<Expression, Void>, LogicalExpressionParser {
        @Override
        public Expression parse(ParseContext ctx, Void unused) {
            return logicalExpression(
                    ctx,
                    () -> ctx.parse(AndParser.class),
                    OR.class
            );
        }
    }
    class AndParser implements ParserElement<Expression, Void>, LogicalExpressionParser {
        @Override
        public Expression parse(ParseContext ctx, Void unused) {
            return logicalExpression(
                    ctx,
                    () -> ctx.parse(BinaryExpressionParser.Equality.class),
                    AND.class
            );
        }

    }
}
