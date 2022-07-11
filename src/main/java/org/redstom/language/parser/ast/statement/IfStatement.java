package org.redstom.language.parser.ast.statement;

import lombok.Builder;
import lombok.Data;
import org.redstom.language.lexer.tokens.keywords.ELSE;
import org.redstom.language.lexer.tokens.keywords.IF;
import org.redstom.language.parser.ast.expression.Expression;
import org.redstom.language.parser.rdp.ParseContext;
import org.redstom.language.parser.rdp.ParserElement;

@Builder
public @Data class IfStatement implements Statement {

    private final Expression test;
    private final Statement consequent;
    private final Statement alternate;

    /**
     * IfStatement
     *  : 'si' Expression Statement
     *  | 'si' Expression Statement 'sinon' Statement
     *  ;
     */
    public static class Parser implements ParserElement<Statement, Void> {

        @Override
        public Statement parse(ParseContext ctx, Void unused) {
            ctx.eat(IF.class);

            Expression test = ctx.parse(Expression.Parser.class);

            Statement consequent = ctx.parse(Statement.Parser.class);

            Statement alternate = null;
            if(ctx.lookahead() instanceof ELSE) {
                    ctx.eat(ELSE.class);
                    alternate = ctx.parse(Statement.Parser.class);
            }

            return IfStatement.builder()
                    .test(test)
                    .consequent(consequent)
                    .alternate(alternate)
                    .build();
        }
    }

}
