package org.redstom.language.lexer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.redstom.language.lexer.tokens.Keyword;
import org.redstom.language.lexer.tokens.Token;

import java.util.function.Function;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Getter
public
class LexerToken {
    public static LexerToken of(Pattern p, Function<String, Token<?>> f) {
        return new LexerToken(p, f);
    }

    public static LexerToken of(Keyword keyword) {
        return new LexerToken(keyword.PATTERN(), new Function<String, Token<?>>() {
            @Override
            @SneakyThrows
            public Token<?> apply(String v) {
                return keyword.getClass().getConstructor().newInstance();
            }
        });
    }

    private final Pattern pattern;
    private final Function<String, Token<?>> function;
}
