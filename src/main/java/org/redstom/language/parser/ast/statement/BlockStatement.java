package org.redstom.language.parser.ast.statement;

import lombok.Builder;
import lombok.Data;
import org.redstom.language.lexer.tokens.braces.CLOSE_BRACE;
import org.redstom.language.lexer.tokens.braces.OPEN_BRACE;
import org.redstom.language.parser.rdp.ParseContext;
import org.redstom.language.parser.rdp.ParserElement;

@Builder
public @Data class BlockStatement implements Statement {

    private final Statement[] body;

    /**
     * BlockStatement
     * : '{' OptStatementList '}'
     * ;
     */
    public static class Parser implements ParserElement<BlockStatement, Void> {

        @Override
        public BlockStatement parse(ParseContext ctx, Void unused) {
            ctx.eat(OPEN_BRACE.class);

            Statement[] body = ctx.lookahead() instanceof CLOSE_BRACE
                    ? new Statement[0]
                    : ctx.parse(Statement.ListParser.class, ListParser.StatementListContext.builder()
                            .stopLookahead(CLOSE_BRACE.class)
                            .build())
                    .toArray(Statement[]::new);

            ctx.eat(CLOSE_BRACE.class);

            return BlockStatement.builder()
                    .body(body)
                    .build();
        }
    }

}
