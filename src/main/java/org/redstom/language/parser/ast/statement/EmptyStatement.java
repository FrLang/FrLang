package org.redstom.language.parser.ast.statement;

import lombok.Builder;
import lombok.Data;
import org.redstom.language.lexer.tokens.syntax.SEMICOLON;
import org.redstom.language.parser.rdp.ParseContext;
import org.redstom.language.parser.rdp.ParserElement;

@Builder
public @Data class EmptyStatement implements Statement {
    /**
     * EmptyStatement
     *  : ';'
     *  ;
     */
    public static class Parser implements ParserElement<EmptyStatement, Void> {
        @Override
        public EmptyStatement parse(ParseContext ctx, Void unused) {
            ctx.eat(SEMICOLON.class);
            return EmptyStatement.builder()
                    .build();
        }
    }
}
