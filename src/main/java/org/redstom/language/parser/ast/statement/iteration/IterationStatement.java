package org.redstom.language.parser.ast.statement.iteration;

import org.redstom.language.lexer.tokens.keywords.DO;
import org.redstom.language.lexer.tokens.keywords.FOR;
import org.redstom.language.lexer.tokens.keywords.WHILE;
import org.redstom.language.parser.ast.statement.Statement;
import org.redstom.language.parser.rdp.ParseContext;
import org.redstom.language.parser.rdp.ParserElement;

public interface IterationStatement extends Statement {

    class Parser implements ParserElement<IterationStatement, Void> {
        @Override
        public IterationStatement parse(ParseContext ctx, Void unused) {
            return switch (ctx.lookahead()) {
                case WHILE ignored -> ctx.parse(WhileStatement.Parser.class);
                case DO ignored -> ctx.parse(DoWhileStatement.Parser.class);
                case FOR ignored -> ctx.parse(ForStatement.Parser.class);
                default -> throw new IllegalStateException("Unexpected value: " + ctx.lookahead());
            };
        }
    }

}
