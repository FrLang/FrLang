package org.redstom.language.lexer.tokens;

import org.redstom.language.lexer.LexerToken;
import org.redstom.language.lexer.tokens.braces.CLOSE_BRACE;
import org.redstom.language.lexer.tokens.braces.OPEN_BRACE;
import org.redstom.language.lexer.tokens.keywords.*;
import org.redstom.language.lexer.tokens.literals.BOOLEAN;
import org.redstom.language.lexer.tokens.literals.NULL;
import org.redstom.language.lexer.tokens.literals.NUMBER;
import org.redstom.language.lexer.tokens.literals.STRING;
import org.redstom.language.lexer.tokens.op.*;
import org.redstom.language.lexer.tokens.par.CLOSE_PAR;
import org.redstom.language.lexer.tokens.par.OPEN_PAR;
import org.redstom.language.lexer.tokens.syntax.ARROW_RIGHT;
import org.redstom.language.lexer.tokens.syntax.COMMA;
import org.redstom.language.lexer.tokens.syntax.IDENTIFIER;
import org.redstom.language.lexer.tokens.syntax.SEMICOLON;

import java.util.function.Function;
import java.util.regex.Pattern;

import static org.redstom.language.lexer.LexerToken.of;

public interface Tokens {

    Function<String, Token<?>> SKIP = s -> null;

    LexerToken[] SPEC = new LexerToken[]{
            // Whitespaces :
            of(Pattern.compile("^\\s+"), SKIP),

            // Comments :
            of(Pattern.compile("^//.*"), SKIP),
            of(Pattern.compile("^/\\*[\\s\\S]*?\\*/"), SKIP),

            // Symbols, delimiters :
            of(new SEMICOLON()),
            of(new OPEN_BRACE()),
            of(new CLOSE_BRACE()),
            of(new OPEN_PAR()),
            of(new CLOSE_PAR()),
            of(new COMMA()),
            of(new ARROW_RIGHT()),

            // Keywords
            of(new VAR()),
            of(new IF()),
            of(new ELSE()),
            of(new WHILE()),
            of(new DO()),
            of(new FOR()),

            // Equality operators: ==, !=, ~=
            of(EQUALITY_OPERATOR.PATTERN, v -> EQUALITY_OPERATOR.builder()
                    .value(v)
                    .build()),

            // Assignment operators :
            of(new SIMPLE_ASSIGN()),
            of(COMPLEX_ASSIGN.PATTERN, v -> COMPLEX_ASSIGN.builder()
                    .value(v)
                    .build()),

            // Math operators : +, -
            of(ADDITIVE_OPERATOR.PATTERN, v -> ADDITIVE_OPERATOR.builder()
                    .value(v)
                    .build()),
            of(MULTIPLICATIVE_OPERATOR.PATTERN, v -> MULTIPLICATIVE_OPERATOR.builder()
                    .value(v)
                    .build()),

            // Relational operators : <, >, <=, >=
            of(RELATIONAL_OPERATOR.PATTERN, v -> RELATIONAL_OPERATOR.builder()
                    .value(v)
                    .build()),

            // Logical operators : et, ou, !
            of(new AND()),
            of(new OR()),
            of(new NOT()),

            // Numbers :
            of(NUMBER.PATTERN, v -> NUMBER.builder()
                    .value(Double.parseDouble(v.replace(",", ".")))
                    .build()),

            // Strings :
            of(STRING.PATTERN, v -> STRING.builder()
                    .value(v)
                    .build()),

            // Null :
            of(NULL.PATTERN, v -> NULL.builder()
                    .value(v)
                    .build()),

            // Booleans :
            of(BOOLEAN.PATTERN, v -> BOOLEAN.builder()
                    .value(switch(v) {
                        case "vrai" -> true;
                        case "faux" -> false;
                        default -> throw new IllegalArgumentException("Invalid boolean value");
                    })
                    .build()),

            // Identifiers :
            of(IDENTIFIER.PATTERN, v -> IDENTIFIER.builder()
                    .value(v)
                    .build())
    };

}
