package org.redstom.language.lexer.tokens.op;

import org.redstom.language.parser.rdp.ParseContext;
import org.redstom.language.parser.rdp.ParserElement;

public interface AssignmentOperator extends Operator {
    /**
     * AssignmentOperator
     *  : SIMPLE_ASSIGN → '='
     *  | COMPLEX_ASSIGN → '+=', '-=', '*=', '/='
     *  ;
     */
    class Parser implements ParserElement<AssignmentOperator, Void> {

        @Override
        public AssignmentOperator parse(ParseContext ctx, Void unused) {
            return switch (ctx.lookahead()) {
                case SIMPLE_ASSIGN assign -> ctx.eat(SIMPLE_ASSIGN.class);
                case COMPLEX_ASSIGN assign -> ctx.eat(COMPLEX_ASSIGN.class);
                default -> throw new IllegalArgumentException("Expected an assignment operator (=, +=, -=, *=, /=), got " + ctx.lookahead());
            };
        }
    }
}
