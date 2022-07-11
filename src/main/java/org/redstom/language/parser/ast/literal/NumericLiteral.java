package org.redstom.language.parser.ast.literal;

import lombok.Builder;
import lombok.Data;
import org.redstom.language.lexer.tokens.literals.NUMBER;
import org.redstom.language.parser.rdp.ParseContext;
import org.redstom.language.parser.rdp.ParserElement;

@Builder
public @Data class NumericLiteral implements Literal {

    private final double value;

    /**
     * NumericLiteral
     *  : NUMBER
     *  ;
     */
    public static class Parser implements ParserElement<NumericLiteral, Void> {
        @Override
        public NumericLiteral parse(ParseContext ctx, Void unused) {
            NUMBER token = ctx.eat(NUMBER.class);
            return NumericLiteral.builder()
                    .value(token.value())
                    .build();
        }
    }
}
