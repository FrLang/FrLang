package org.redstom.language.parser.ast.statement.iteration;

import lombok.Builder;
import lombok.Data;
import org.redstom.language.lexer.tokens.keywords.FOR;
import org.redstom.language.lexer.tokens.keywords.VAR;
import org.redstom.language.lexer.tokens.op.SIMPLE_ASSIGN;
import org.redstom.language.lexer.tokens.par.CLOSE_PAR;
import org.redstom.language.lexer.tokens.par.OPEN_PAR;
import org.redstom.language.lexer.tokens.syntax.ARROW_RIGHT;
import org.redstom.language.lexer.tokens.syntax.SEMICOLON;
import org.redstom.language.parser.ast.expression.Expression;
import org.redstom.language.parser.ast.expression.Identifier;
import org.redstom.language.parser.ast.statement.Statement;
import org.redstom.language.parser.ast.statement.VariableStatement;
import org.redstom.language.parser.rdp.ParseContext;
import org.redstom.language.parser.rdp.ParserElement;

public interface ForStatement extends IterationStatement {

    /**
     * ForStatement
     * : 'pour' ForRangeStatement
     * | 'pour' ForIntStatement
     * ;
     */
    class Parser implements ParserElement<ForStatement, Void> {
        @Override
        public org.redstom.language.parser.ast.statement.iteration.ForStatement parse(ParseContext ctx, Void unused) {
            ctx.eat(FOR.class);

            return switch (ctx.lookahead()) {
                case VAR ignored -> ctx.parse(ForIntStatement.Parser.class);
                default -> ctx.parse(ForRangeStatement.Parser.class);
            };
        }
    }

    @Builder
    @Data
    class ForRangeStatement implements ForStatement {

        private final Init init;
        private final Statement body;

        /**
         * ForRangeStatement
         * : ForStatementInitList Statement
         * ;
         */
        public static class Parser implements ParserElement<ForRangeStatement, Void> {
            @Override
            public ForRangeStatement parse(ParseContext ctx, Void unused) {
                ForRangeStatement.Init init = ctx.parse(ForRangeStatement.Init.Parser.class);

                Statement body = ctx.parse(Statement.Parser.class);

                return ForRangeStatement.builder()
                        .init(init)
                        .body(body)
                        .build();
            }
        }

        @Builder
        public static @Data class Init {

            private final Identifier identifier;
            private final Expression from;
            private final Expression to;
            private final Expression step;

            /**
             * ForRangeStatementInit
             * : Identifier '=' Expression '->' Expression
             * | Identifier '=' Expression '->' Expression '(' Expression ')'
             * ;
             */
            public static class Parser implements ParserElement<Init, Void> {
                @Override
                public Init parse(ParseContext ctx, Void unused) {
                    Identifier identifier = ctx.parse(Identifier.Parser.class);
                    ctx.eat(SIMPLE_ASSIGN.class);
                    Expression from = ctx.parse(Expression.Parser.class);

                    ctx.eat(ARROW_RIGHT.class);
                    Expression to = ctx.parse(Expression.Parser.class);

                    Expression step = switch (ctx.lookahead()) {
                        case OPEN_PAR ignored -> {
                            ctx.eat(OPEN_PAR.class);
                            Expression ex = ctx.parse(Expression.Parser.class);
                            ctx.eat(CLOSE_PAR.class);
                            yield ex;
                        }
                        default -> null;
                    };

                    return Init.builder()
                            .identifier(identifier)
                            .from(from)
                            .to(to)
                            .step(step)
                            .build();
                }
            }

        }
    }

    @Builder
    @Data
    class ForIntStatement implements ForStatement {

        private VariableStatement init;
        private Expression test;
        private Expression update;

        private Statement body;

        /**
         * ForIntStatement
         * : VariableInitStatement ; Expression ';' Expression Statement
         * ;
         */
        public static class Parser implements ParserElement<ForIntStatement, Void> {

            @Override
            public ForIntStatement parse(ParseContext ctx, Void unused) {

                VariableStatement stmt = ctx.parse(VariableStatement.InitParser.class);
                ctx.eat(SEMICOLON.class);

                Expression test = ctx.parse(Expression.Parser.class);
                ctx.eat(SEMICOLON.class);

                Expression update = ctx.parse(Expression.Parser.class);

                Statement body = ctx.parse(Statement.Parser.class);

                return ForIntStatement.builder()
                        .init(stmt)
                        .test(test)
                        .update(update)
                        .body(body)
                        .build();
            }
        }
    }
}
