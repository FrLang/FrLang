package org.redstom.language.lexer.tokens.syntax;

import lombok.Builder;
import lombok.Data;
import org.redstom.language.lexer.tokens.Token;

import java.util.regex.Pattern;

@Builder
public @Data class IDENTIFIER implements Token<String> {

    public static final Pattern PATTERN = Pattern.compile("^\\w+");

    private final String value;

}
