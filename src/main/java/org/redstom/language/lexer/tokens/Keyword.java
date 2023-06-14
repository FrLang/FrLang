package org.redstom.language.lexer.tokens;

import lombok.Getter;

import java.util.regex.Pattern;

@Getter
public class Keyword implements Token<String> {

    private final Pattern pattern;
    private final String value;

    public Keyword(String keyword) {
        this.pattern = Pattern.compile("^\\b" + keyword + "\\b");
        this.value = keyword;
    }

    protected Keyword(Pattern pattern, String value) {
        this.pattern = pattern;
        this.value = value;
    }
}
