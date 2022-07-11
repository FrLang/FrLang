package org.redstom.language.parser.rdp;

import lombok.*;
import org.redstom.language.lexer.tokens.Token;
import org.redstom.language.parser.Parser;
import org.redstom.language.utils.Ref;

import java.util.HashMap;
import java.util.Map;

import static org.redstom.language.utils.Ref.ref;

@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public
class ParseContext {

    public static ParseContext create(Parser parser) {
        ParseContext ctx = new ParseContext(parser);
        ctx.lookahead.set(parser.lookahead());
        return ctx;
    }

    @Getter(AccessLevel.PRIVATE)
    private final Map<Class<?>, Object> parserElements = new HashMap<>();

    @Getter(AccessLevel.PRIVATE)
    private final Parser parser;

    private final Ref<Token<?>> lookahead = ref(null);

    public Token<?> lookahead() {
        return lookahead.get();
    }

    public <T extends Token<?>> T eat(Class<T> clazz) {
        T token = parser.eat(clazz);
        lookahead.set(parser.lookahead());
        return token;
    }

    @SneakyThrows
    public <R, S, T extends ParserElement<R, S>> R parse(Class<T> clazz, S context) {
        T instance;
        if (!parserElements.containsKey(clazz)) {
            instance = clazz.getDeclaredConstructor().newInstance();
            parserElements.put(clazz, instance);
        } else {
            instance = (T) parserElements.get(clazz);
        }

        return instance.parse(this, context);
    }

    public <R, S, T extends ParserElement<R, S>> R parse(Class<T> clazz) {
        return parse(clazz, null);
    }
}
