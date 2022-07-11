package org.redstom.language.parser.ast.statement;

import lombok.Builder;
import lombok.Data;
import org.redstom.language.lexer.tokens.Token;
import org.redstom.language.lexer.tokens.braces.OPEN_BRACE;
import org.redstom.language.lexer.tokens.keywords.*;
import org.redstom.language.lexer.tokens.syntax.SEMICOLON;
import org.redstom.language.parser.ast.ASTNode;
import org.redstom.language.parser.ast.expression.ExpressionStatement;
import org.redstom.language.parser.ast.statement.iteration.IterationStatement;
import org.redstom.language.parser.rdp.ParseContext;
import org.redstom.language.parser.rdp.ParserElement;

import java.util.ArrayList;
import java.util.List;

public interface Statement extends ASTNode {
    /**
     * StatementList
     *  : Statement
     *  | StatementList Statement
     *  ;
     */
    class ListParser implements ParserElement<List<Statement>, ListParser.StatementListContext> {
        @Override
        public List<Statement> parse(ParseContext ctx, StatementListContext context) {
            List<Statement> statements = new ArrayList<>();

            while (ctx.lookahead() != null && (context.stopLookahead() == null
                    || !context.stopLookahead().isInstance(ctx.lookahead()))) {
                statements.add(ctx.parse(Statement.Parser.class));
            }

            return statements;
        }

        @Data
        @Builder
        public static class StatementListContext {
            private Class<? extends Token<?>> stopLookahead;
        }
    }

    /**
     * Statement
     *  : ExpressionStatement
     *  | BlockStatement
     *  | EmptyStatement
     *  | VariableStatement
     *  | IfStatement
     *  | IterationStatement
     *  ;
     */
    class Parser implements ParserElement<Statement, Void> {
        @Override
        public Statement parse(ParseContext ctx, Void unused) {
            return switch(ctx.lookahead()) {
                case SEMICOLON ignored -> ctx.parse(EmptyStatement.Parser.class);
                case OPEN_BRACE ignored -> ctx.parse(BlockStatement.Parser.class);
                case VAR ignored -> ctx.parse(VariableStatement.Parser.class);
                case IF ignored -> ctx.parse(IfStatement.Parser.class);
                case IterativeToken ignored -> ctx.parse(IterationStatement.Parser.class);
                default -> ctx.parse(ExpressionStatement.Parser.class);
            };
        }
    }
}
