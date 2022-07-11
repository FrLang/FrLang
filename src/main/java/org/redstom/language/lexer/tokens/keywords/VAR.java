package org.redstom.language.lexer.tokens.keywords;

import lombok.ToString;
import org.redstom.language.lexer.tokens.Keyword;

@ToString
public class VAR extends Keyword {
    public VAR() {
        super("var");
    }
}
