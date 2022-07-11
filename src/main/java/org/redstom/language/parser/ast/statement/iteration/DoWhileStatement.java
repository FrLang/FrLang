package org.redstom.language.parser.ast.statement.iteration;

import lombok.Builder;
import lombok.Data;
import org.redstom.language.lexer.tokens.keywords.DO;
import org.redstom.language.lexer.tokens.keywords.WHILE;
import org.redstom.language.lexer.tokens.syntax.SEMICOLON;
import org.redstom.language.parser.ast.expression.Expression;
import org.redstom.language.parser.ast.statement.Statement;
import org.redstom.language.parser.rdp.ParseContext;
import org.redstom.language.parser.rdp.ParserElement;

@Builder
@Data
public
class DoWhileStatement implements IterationStatement {

    private final Expression test;
    private final Statement body;

    /**
     * DoWhileStatement
     * : 'faire' Statement 'tant que' Expression ';'
     * ;
     */
    public static class Parser implements ParserElement<org.redstom.language.parser.ast.statement.iteration.DoWhileStatement, Void> {
        @Override
        public org.redstom.language.parser.ast.statement.iteration.DoWhileStatement parse(ParseContext ctx, Void unused) {

            ctx.eat(DO.class);
            Statement body = ctx.parse(Statement.Parser.class);

            ctx.eat(WHILE.class);
            Expression test = ctx.parse(Expression.Parser.class);

            ctx.eat(SEMICOLON.class);

            return org.redstom.language.parser.ast.statement.iteration.DoWhileStatement.builder()
                    .test(test)
                    .body(body)
                    .build();
        }
    }
}
