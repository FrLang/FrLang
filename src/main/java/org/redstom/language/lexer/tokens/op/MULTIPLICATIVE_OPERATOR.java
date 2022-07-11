package org.redstom.language.lexer.tokens.op;

import lombok.Builder;
import lombok.Data;

import java.util.regex.Pattern;

@Builder
public @Data class MULTIPLICATIVE_OPERATOR implements Operator {

    public static final Pattern PATTERN = Pattern.compile("^[*/]");

    private final String value;
}
