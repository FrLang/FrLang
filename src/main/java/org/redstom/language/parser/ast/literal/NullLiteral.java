package org.redstom.language.parser.ast.literal;

import lombok.Builder;
import lombok.Data;
import org.redstom.language.lexer.tokens.literals.NULL;
import org.redstom.language.parser.rdp.ParseContext;
import org.redstom.language.parser.rdp.ParserElement;

@Builder
public @Data class NullLiteral implements Literal {

    /**
     * NullLiteral
     * : 'nul'
     * ;
     */
    public static class Parser implements ParserElement<NullLiteral, Void> {
        @Override
        public NullLiteral parse(ParseContext ctx, Void unused) {
            ctx.eat(NULL.class);
            return NullLiteral.builder()
                    .build();
        }
    }
}
