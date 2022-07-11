package org.redstom.language.parser.ast.statement.iteration;

import lombok.Builder;
import lombok.Data;
import org.redstom.language.lexer.tokens.keywords.WHILE;
import org.redstom.language.parser.ast.expression.Expression;
import org.redstom.language.parser.ast.statement.Statement;
import org.redstom.language.parser.rdp.ParseContext;
import org.redstom.language.parser.rdp.ParserElement;

@Builder
@Data
public
class WhileStatement implements IterationStatement {

    private final Expression test;
    private final Statement body;

    /**
     * WhileStatement
     * : 'tant que' Expression Statement
     * ;
     */
    public static class Parser implements ParserElement<WhileStatement, Void> {
        @Override
        public WhileStatement parse(ParseContext ctx, Void unused) {
            ctx.eat(WHILE.class);

            Expression test = ctx.parse(Expression.Parser.class);
            Statement body = switch (ctx.lookahead()) {
                case null -> null;
                default -> ctx.parse(Statement.Parser.class);
            };

            return org.redstom.language.parser.ast.statement.iteration.WhileStatement.builder()
                    .test(test)
                    .body(body)
                    .build();
        }
    }
}
