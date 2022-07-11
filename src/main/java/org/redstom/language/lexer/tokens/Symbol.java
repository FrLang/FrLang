package org.redstom.language.lexer.tokens;

import java.util.regex.Pattern;

public class Symbol extends Keyword {
    public Symbol(String value, boolean escape) {
        super(Pattern.compile("^" + (escape ? "\\" : "") + value), value);
    }
}
