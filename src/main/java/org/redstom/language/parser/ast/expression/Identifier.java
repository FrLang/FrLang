package org.redstom.language.parser.ast.expression;

import lombok.Builder;
import lombok.Data;
import org.redstom.language.lexer.tokens.syntax.IDENTIFIER;
import org.redstom.language.parser.rdp.ParseContext;
import org.redstom.language.parser.rdp.ParserElement;

@Builder
public @Data class Identifier implements Expression {

    private final String name;

    public static class Parser implements ParserElement<Identifier, Void> {
        @Override
        public Identifier parse(ParseContext ctx, Void unused) {
            return Identifier.builder()
                    .name(ctx.eat(IDENTIFIER.class).value())
                    .build();
        }
    }
}
