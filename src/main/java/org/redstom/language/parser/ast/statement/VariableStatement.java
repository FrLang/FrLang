package org.redstom.language.parser.ast.statement;

import lombok.Builder;
import lombok.Data;
import org.redstom.language.lexer.tokens.keywords.VAR;
import org.redstom.language.lexer.tokens.syntax.SEMICOLON;
import org.redstom.language.parser.rdp.ParseContext;
import org.redstom.language.parser.rdp.ParserElement;

@Builder
public @Data class VariableStatement implements Statement {

    private final VariableDeclaration[] declarations;

    /**
     * VariableStatement
     * : VariableInitStatement ';'
     * ;
     */
    public static class Parser implements ParserElement<VariableStatement, Void> {
        @Override
        public VariableStatement parse(ParseContext ctx, Void unused) {
            VariableStatement stmt = ctx.parse(VariableStatement.InitParser.class);
            ctx.eat(SEMICOLON.class);

            return stmt;
        }
    }

    /**
     * VariableInitStatement
     *  : 'var' VariableDeclarationList
     *  ;
     */
    public static class InitParser implements ParserElement<VariableStatement, Void> {

        @Override
        public VariableStatement parse(ParseContext ctx, Void unused) {
            ctx.eat(VAR.class);
            VariableDeclaration[] declarations = ctx.parse(VariableDeclaration.ListParser.class)
                    .toArray(VariableDeclaration[]::new);

            return VariableStatement.builder()
                    .declarations(declarations)
                    .build();
        }
    }
}
