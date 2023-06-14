package org.redstom.language.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redstom.language.parser.ast.expression.ExpressionStatement;
import org.redstom.language.parser.ast.literal.NumericLiteral;
import org.redstom.language.parser.ast.literal.StringLiteral;
import org.redstom.language.parser.ast.Program;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LiteralsTest {

    private Parser parser;

    @BeforeEach
    void setUp() {
        this.parser = new Parser();
    }

    @Test
    void doubleQuotedStringLiteral() {
        Program result = parser.parse("""
                "42";
                """);

        assertEquals(1, result.body().length);
        assertTrue(result.body()[0] instanceof ExpressionStatement);

        ExpressionStatement stmt = (ExpressionStatement) result.body()[0];
        assertTrue(stmt.expression() instanceof StringLiteral);

        StringLiteral literal = (StringLiteral) stmt.expression();
        assertEquals("42", literal.value());
    }

    @Test
    void singleQuotedStringLiteral() {
        Program result = parser.parse("""
                '42';
                """);

        assertEquals(1, result.body().length);
        assertTrue(result.body()[0] instanceof ExpressionStatement);

        ExpressionStatement stmt = (ExpressionStatement) result.body()[0];
        assertTrue(stmt.expression() instanceof StringLiteral);

        StringLiteral literal = (StringLiteral) stmt.expression();
        assertEquals("42", literal.value());
    }

    @Test
    void numericLiteral() {
        Program result = parser.parse("""
                42;
                """);

        assertEquals(1, result.body().length);
        assertTrue(result.body()[0] instanceof ExpressionStatement);

        ExpressionStatement stmt = (ExpressionStatement) result.body()[0];
        assertTrue(stmt.expression() instanceof NumericLiteral);

        NumericLiteral literal = (NumericLiteral) stmt.expression();
        assertEquals(42, literal.value());
    }
}
