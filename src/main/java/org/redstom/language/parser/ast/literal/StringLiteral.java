package org.redstom.language.parser.ast.literal;

import lombok.Builder;
import lombok.Data;
import org.redstom.language.lexer.tokens.literals.STRING;
import org.redstom.language.parser.rdp.ParseContext;
import org.redstom.language.parser.rdp.ParserElement;

@Builder
public @Data class StringLiteral implements Literal {

    private final String value;

    /**
     * StringLiteral
     * : STRING
     * ;
     */
    public static class Parser implements ParserElement<StringLiteral, Void> {

        @Override
        public StringLiteral parse(ParseContext ctx, Void unused) {
            STRING token = ctx.eat(STRING.class);
            return StringLiteral.builder()
                    .value(token
                            .value()
                            .substring(1)
                            .substring(0, token.value().length() - 2)
                    )
                    .build();
        }
    }

}
