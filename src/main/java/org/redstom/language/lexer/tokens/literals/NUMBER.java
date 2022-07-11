package org.redstom.language.lexer.tokens.literals;

import lombok.Builder;
import lombok.Data;

import java.util.regex.Pattern;

@Builder
public @Data class NUMBER implements LiteralToken<Double> {

    public static final Pattern PATTERN = Pattern.compile("^\\d+(.\\d+)?");

    private final Double value;

}
