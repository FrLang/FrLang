package org.redstom.language.lexer.tokens.literals;

import lombok.Builder;
import lombok.Data;

import java.util.regex.Pattern;

@Builder
public @Data class BOOLEAN implements LiteralToken<Boolean> {
    public static final Pattern PATTERN = Pattern.compile("^(vrai|faux)");

    private final Boolean value;
}
