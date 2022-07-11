package org.redstom.language.parser;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.redstom.language.exceptions.Error;
import org.redstom.language.exceptions.SyntaxError;
import org.redstom.language.lexer.Lexer;
import org.redstom.language.lexer.tokens.Keyword;
import org.redstom.language.lexer.tokens.Token;
import org.redstom.language.parser.ast.Program;
import org.redstom.language.parser.rdp.ParseContext;

public class Parser {

    private final Lexer lexer;
    private String string;

    @Getter
    private Token<?> lookahead;
    @Getter
    @Setter
    private int line;
    private ParseContext ctx;

    /**
     * Initializes the parser.
     */
    public Parser() {
        this.string = "";
        this.lexer = new Lexer(this);
    }

    /**
     * Parses a string into an AST.
     */
    public Program parse(String string) {
        this.string = string;
        this.lexer.init(string);

        this.lookahead = this.lexer.nextToken();
        try {
            this.ctx = ParseContext.create(this);
            return ctx.parse(Program.Parser.class);
        } catch (Throwable ex) {
            if (ex instanceof Error e) {
                e.show();
            } else {
                if (lookahead != null) {
                    new SyntaxError("Unexpected \"" + lookahead().value() + "\" on line " + line + " ! ").show();
                } else {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

    @SneakyThrows
    public <T extends Token<?>> T eat(Class<T> clazz) {
        Token<?> token = this.lookahead;

        if (token == null) {
            throw new SyntaxError("Unexpected end of input on line " + line() + ", expected : \"" + clazz.getSimpleName() + "\"");
        }

        if (!(clazz.isInstance(token))) {
            String name = clazz.getSimpleName();
            if (Keyword.class.isAssignableFrom(clazz)) {
                Keyword keyword = (Keyword) clazz.getConstructor().newInstance();
                name = keyword.value();
            }
            throw new SyntaxError("Unexpected \"" + token.value() + "\" on line " + line() + ", expected : \"" + name + "\"");
        }

        this.lookahead = this.lexer.nextToken();

        return (T) token;
    }


}
