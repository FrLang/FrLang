package org.redstom.language.parser.ast.statement;

import lombok.Builder;
import lombok.Data;
import org.redstom.language.lexer.tokens.op.SIMPLE_ASSIGN;
import org.redstom.language.lexer.tokens.syntax.COMMA;
import org.redstom.language.lexer.tokens.syntax.SEMICOLON;
import org.redstom.language.parser.ast.expression.AssignmentExpression;
import org.redstom.language.parser.ast.expression.Expression;
import org.redstom.language.parser.ast.expression.Identifier;
import org.redstom.language.parser.rdp.ParseContext;
import org.redstom.language.parser.rdp.ParserElement;

import java.util.ArrayList;
import java.util.List;

import static org.redstom.language.utils.JavaUtils.eval;

@Builder
public @Data class VariableDeclaration implements Expression {
    private Identifier identifier;
    private Expression initializer;

    /**
     * VariableDeclarationList
     *  : VariableDeclaration
     *  | VariableDeclarationList ',' VariableDeclaration
     *  ;
     */
    public static class ListParser implements ParserElement<List<VariableDeclaration>, Void> {
        @Override
        public List<VariableDeclaration> parse(ParseContext ctx, Void unused) {
            List<VariableDeclaration> declarations = new ArrayList<>();
            do {
                declarations.add(ctx.parse(VariableDeclaration.Parser.class));
            } while (ctx.lookahead() instanceof COMMA && eval(ctx.eat(COMMA.class)));

            return declarations;
        }
    }

    /**
     * VariableDeclaration
     *  : Identifier OptVariableInitializer
     */
    public static class Parser implements ParserElement<VariableDeclaration, Void> {
        @Override
        public VariableDeclaration parse(ParseContext ctx, Void unused) {
            Identifier id = ctx.parse(Identifier.Parser.class);

            Expression init = !(ctx.lookahead() instanceof SEMICOLON) && !(ctx.lookahead() instanceof COMMA)
                    ? ctx.parse(VariableInitializerParser.class)
                    : null;

            return VariableDeclaration.builder()
                    .identifier(id)
                    .initializer(init)
                    .build();
        }
    }

    /**
     * VariableInitializer
     *  : '=' AssignmentExpression
     *  ;
     */
    public static class VariableInitializerParser implements ParserElement<Expression, Void> {
        @Override
        public Expression parse(ParseContext ctx, Void unused) {
            ctx.eat(SIMPLE_ASSIGN.class);
            return ctx.parse(AssignmentExpression.Parser.class);
        }
    }
}
