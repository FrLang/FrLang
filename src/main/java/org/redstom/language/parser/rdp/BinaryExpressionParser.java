package org.redstom.language.parser.rdp;

import org.redstom.language.lexer.tokens.Token;
import org.redstom.language.lexer.tokens.keywords.NOT;
import org.redstom.language.lexer.tokens.op.*;
import org.redstom.language.parser.ast.expression.BinaryExpression;
import org.redstom.language.parser.ast.expression.Expression;
import org.redstom.language.parser.ast.expression.LeftHandSide;
import org.redstom.language.parser.ast.expression.UnaryExpression;

import java.util.function.Supplier;

public interface BinaryExpressionParser {

    default Expression binaryExpression(ParseContext ctx,
                                        Supplier<Expression> sup,
                                        Class<? extends Operator> clazz) {
        Expression left = sup.get();

        while (clazz.isInstance(ctx.lookahead())) {
            Operator operator = ctx.eat(clazz);

            Expression right = sup.get();

            left = BinaryExpression.builder()
                    .left(left)
                    .operator(operator.value())
                    .right(right)
                    .build();
        }

        return left;
    }

    /**
     * EqualityExpression
     *  : RelationalExpression
     *  | EqualityExpression EQUALITY_OPERATOR RelationalExpression
     *  ;
     */
    class Equality implements ParserElement<Expression, Void>, BinaryExpressionParser {
        @Override
        public Expression parse(ParseContext ctx, Void unused) {
            return binaryExpression(
                    ctx,
                    () -> ctx.parse(Relational.class),
                    EQUALITY_OPERATOR.class
            );
        }
    }

    /**
     * RelationalExpression
     *  : AdditiveExpression
     *  | RelationalExpression RELATIONAL_OPERATOR AdditiveExpression
     *  ;
     */
    class Relational implements ParserElement<Expression, Void>, BinaryExpressionParser {
        @Override
        public Expression parse(ParseContext ctx, Void unused) {
            return binaryExpression(
                    ctx,
                    () -> ctx.parse(Additive.class),
                    RELATIONAL_OPERATOR.class
            );
        }
    }

    /**
     * AdditiveExpression
     *  : MultiplicativeExpression
     *  | AdditiveExpression ADDITIVE_OPERATOR MultiplicativeExpression
     *  ;
     */
    class Additive implements ParserElement<Expression, Void>, BinaryExpressionParser {
        @Override
        public Expression parse(ParseContext ctx, Void unused) {
            return binaryExpression(
                    ctx,
                    () -> ctx.parse(Multiplicative.class),
                    ADDITIVE_OPERATOR.class
            );
        }
    }

    /**
     * MultiplicativeExpression
     *  : UnaryExpression
     *  | MultiplicativeExpression MULTIPLICATIVE_OPERATOR UnaryExpression
     *  ;
     */
    class Multiplicative implements ParserElement<Expression, Void>, BinaryExpressionParser {
        @Override
        public Expression parse(ParseContext ctx, Void unused) {
            return binaryExpression(
                    ctx,
                    () -> ctx.parse(Unary.class),
                    MULTIPLICATIVE_OPERATOR.class
            );
        }
    }

    /**
     * UnaryExpression
     *  : LeftHandSideExpression
     *  | ADDITIVE_OPERATOR UnaryExpression
     *  | NOT UnaryExpression
     *  ;
     */
    class Unary implements ParserElement<Expression, Void>, BinaryExpressionParser {
        @Override
        public Expression parse(ParseContext ctx, Void unused) {
            Token<String> operator = switch (ctx.lookahead()) {
                case ADDITIVE_OPERATOR op -> ctx.eat(op.getClass());
                case NOT op -> ctx.eat(op.getClass());
                default -> null;
            };

            if(operator != null) {
                return UnaryExpression.builder()
                        .operator(operator.value())
                        .argument(ctx.parse(Unary.class))
                        .build();
            }

            return ctx.parse(LeftHandSide.Parser.class);
        }
    }
}
