package org.redstom.language.lexer.tokens;

import lombok.Getter;

import java.util.regex.Pattern;

@Getter
public class Keyword implements Token<String> {

    private final Pattern PATTERN;
    private final String value;

    public Keyword(String keyword) {
        this.PATTERN = Pattern.compile("^\\b" + keyword + "\\b");
        this.value = keyword;
    }

    protected Keyword(Pattern PATTERN, String value) {
        this.PATTERN = PATTERN;
        this.value = value;
    }
}
