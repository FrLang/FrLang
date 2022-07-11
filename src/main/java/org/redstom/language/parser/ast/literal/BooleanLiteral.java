package org.redstom.language.parser.ast.literal;

import lombok.Builder;
import lombok.Data;
import org.redstom.language.lexer.tokens.literals.BOOLEAN;
import org.redstom.language.parser.rdp.ParseContext;
import org.redstom.language.parser.rdp.ParserElement;

@Builder
public @Data class BooleanLiteral implements Literal {

    private final boolean value;

    public static class Parser implements ParserElement<BooleanLiteral, Void> {

        @Override
        public BooleanLiteral parse(ParseContext ctx, Void unused) {
            boolean value = ctx.eat(BOOLEAN.class).value();
            return BooleanLiteral.builder()
                    .value(value)
                    .build();
        }
    }

}
