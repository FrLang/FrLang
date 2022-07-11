package org.redstom.language.parser.ast.literal;

import lombok.SneakyThrows;
import org.redstom.language.exceptions.SyntaxError;
import org.redstom.language.lexer.tokens.literals.BOOLEAN;
import org.redstom.language.lexer.tokens.literals.NULL;
import org.redstom.language.lexer.tokens.literals.NUMBER;
import org.redstom.language.lexer.tokens.literals.STRING;
import org.redstom.language.parser.ast.expression.Expression;
import org.redstom.language.parser.rdp.ParseContext;
import org.redstom.language.parser.rdp.ParserElement;

public interface Literal extends Expression {
    /**
     * Literal
     *  : NumericLiteral
     *  | StringLiteral
     *  | BooleanLiteral
     *  | NullLiteral
     *  ;
     */
    class Parser implements ParserElement<Literal, Void> {
        @Override
        @SneakyThrows
        public Literal parse(ParseContext ctx, Void unused) {
            return switch (ctx.lookahead()) {
                case NUMBER ignored  -> ctx.parse(NumericLiteral.Parser.class);
                case STRING ignored  -> ctx.parse(StringLiteral.Parser.class);
                case BOOLEAN ignored -> ctx.parse(BooleanLiteral.Parser.class);
                case NULL ignored    -> ctx.parse(NullLiteral.Parser.class);
                default              -> throw new SyntaxError("Literal: unexpected literal production");
            };
        }
    }
}
