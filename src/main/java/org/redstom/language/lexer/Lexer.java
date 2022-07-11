package org.redstom.language.lexer;

import lombok.SneakyThrows;
import org.redstom.language.exceptions.SyntaxError;
import org.redstom.language.lexer.tokens.Token;
import org.redstom.language.lexer.tokens.Tokens;
import org.redstom.language.parser.Parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.redstom.language.lexer.tokens.Tokens.SKIP;

/**
 * Lexer class.
 * <p>
 * Lazily pulls a token from a stream.
 */
public class Lexer {

    private final Parser parser;
    private String string;
    private int cursor;

    public Lexer(Parser parser) {
        this.parser = parser;
    }

    /**
     * Initializes the string/
     */
    public void init(String string) {
        this.string = string;
        this.cursor = 0;
    }

    /**
     * Whether we still have more tokens.
     */
    public boolean hasMoreTokens() {
        return this.cursor < this.string.length();
    }

    /**
     * Obtain next token.
     */
    @SneakyThrows
    public Token<?> nextToken() {
        if (!this.hasMoreTokens()) {
            return null;
        }

        String string = this.string.substring(this.cursor);

        for (LexerToken spec : Tokens.SPEC) {
            String tokenValue = this.match(spec.pattern(), string);

            if(tokenValue == null) {
                continue;
            }

            if(spec.function() == SKIP) {
                return this.nextToken();
            }

            parser.line(this.string.split("\n").length - string.split("\n").length + 1);
            return spec.function().apply(tokenValue);
        }

        throw new SyntaxError("Unexpected token: \"" + string.charAt(0) + "\"");
    }

    private String match(Pattern pattern, String s) {
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            this.cursor += matcher.end() - matcher.start();
            return matcher.group();
        }
        return null;
    }

}
