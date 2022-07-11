package org.redstom.language.lexer.tokens.literals;

import lombok.Builder;
import lombok.Data;

import java.util.regex.Pattern;

@Builder
public @Data class NULL implements LiteralToken<String> {
    public static final Pattern PATTERN = Pattern.compile("^nul");

    private final String value;
}
