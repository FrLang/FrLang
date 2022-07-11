package org.redstom.language.lexer.tokens.op;

import org.redstom.language.lexer.tokens.Symbol;

public class SIMPLE_ASSIGN extends Symbol implements AssignmentOperator {
    public SIMPLE_ASSIGN() {
        super("=", false);
    }
}
