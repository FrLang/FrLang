package org.redstom.language.parser.ast;

import lombok.Builder;
import lombok.Data;
import org.redstom.language.parser.ast.statement.Statement.ListParser;
import org.redstom.language.parser.ast.statement.Statement.ListParser.StatementListContext;
import org.redstom.language.parser.rdp.ParseContext;
import org.redstom.language.parser.rdp.ParserElement;

@Builder
public @Data class Program {

    private ASTNode[] body;

    /**
     * Program
     *  : StatementList
     *  ;
     */
    public static class Parser implements ParserElement<Program, Void> {
        @Override
        public Program parse(ParseContext ctx, Void v) {
            return builder()
                    .body(ctx.parse(ListParser.class,
                                    StatementListContext.builder().build())
                            .toArray(ASTNode[]::new))
                    .build();
        }
    }
}
