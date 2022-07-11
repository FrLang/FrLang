package org.redstom.language.parser.ast.expression;

import org.redstom.language.lexer.tokens.Token;
import org.redstom.language.lexer.tokens.literals.LiteralToken;
import org.redstom.language.lexer.tokens.par.OPEN_PAR;
import org.redstom.language.lexer.tokens.syntax.IDENTIFIER;
import org.redstom.language.parser.ast.literal.Literal;
import org.redstom.language.parser.ast.statement.Statement;
import org.redstom.language.parser.rdp.ParseContext;
import org.redstom.language.parser.rdp.ParserElement;

public interface Expression extends Statement {

    /**
     * Expression
     *  : AssignmentExpression
     *  ;
     */
    class Parser implements ParserElement<Expression, Void> {

        @Override
        public Expression parse(ParseContext ctx, Void unused) {
            return ctx.parse(AssignmentExpression.Parser.class);
        }
    }

    /**
     * PrimaryExpression
     *  : Literal
     *  | ParenthesizedExpression
     *  | Identifier
     *  ;
     */
    class PrimaryParser implements ParserElement<Expression, Void> {
        @Override
        public Expression parse(ParseContext ctx, Void unused) {
            if(this.isLiteral(ctx.lookahead())) {
                return ctx.parse(Literal.Parser.class);
            }

            return switch(ctx.lookahead()) {
                case OPEN_PAR ignored -> ctx.parse(ParenthesizedExpression.Parser.class);
                case IDENTIFIER ignored -> ctx.parse(Identifier.Parser.class);
                default -> ctx.parse(LeftHandSide.Parser.class);
            };
        }

        private boolean isLiteral(Token<?> lookahead) {
            return lookahead instanceof LiteralToken<?>;
        }
    }

}
