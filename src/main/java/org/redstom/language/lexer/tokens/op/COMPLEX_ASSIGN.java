package org.redstom.language.lexer.tokens.op;

import lombok.Builder;
import lombok.Data;
import org.redstom.language.lexer.tokens.Token;

import java.util.regex.Pattern;

@Builder
public @Data class COMPLEX_ASSIGN implements Token<String>, AssignmentOperator {

    public static final Pattern PATTERN = Pattern.compile("^[*/+\\-]=");

    private final String value;

}
