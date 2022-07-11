package org.redstom.language.parser.ast.expression;

import lombok.Builder;
import lombok.Data;
import org.redstom.language.lexer.tokens.op.AssignmentOperator;
import org.redstom.language.parser.rdp.LogicalExpressionParser.OrParser;
import org.redstom.language.parser.rdp.ParseContext;
import org.redstom.language.parser.rdp.ParserElement;

import static org.redstom.language.utils.ParserUtils.checkValidAssignmentTarget;

@Builder
public @Data class AssignmentExpression implements Expression {

    private final String operator;
    private final Expression left;
    private final Expression right;


    /**
     * AssignmentExpression
     *  : OrExpression
     *  | OrExpression AssignmentOperator AssignmentExpression
     *  ;
     */
    public static class Parser implements ParserElement<Expression, Void> {

        @Override
        public Expression parse(ParseContext ctx, Void unused) {
            Expression left = ctx.parse(OrParser.class);

            if(!(ctx.lookahead() instanceof AssignmentOperator)) {
                return left;
            }

            return AssignmentExpression.builder()
                    .operator(ctx.parse(AssignmentOperator.Parser.class).value())
                    .left(checkValidAssignmentTarget(left))
                    .right(ctx.parse(AssignmentExpression.Parser.class))
                    .build();
        }
    }

}
