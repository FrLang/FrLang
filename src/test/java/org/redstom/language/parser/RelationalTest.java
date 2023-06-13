package org.redstom.language.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redstom.language.parser.ast.Program;
import org.redstom.language.parser.ast.expression.BinaryExpression;
import org.redstom.language.parser.ast.expression.ExpressionStatement;
import org.redstom.language.parser.ast.expression.Identifier;
import org.redstom.language.parser.ast.literal.NumericLiteral;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RelationalTest {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapterFactory(Serializer.of(Object.class, "type"))
            .serializeNulls()
            .create();

    private Parser parser;

    @BeforeEach
    void setUp() {
        this.parser = new Parser();
    }

    @Test
    void testSimpleGreaterRelational() {
        Program program = parser.parse("""
                x > 10;
                """);

        assertEquals(1, program.body().length);
        assertTrue(program.body()[0] instanceof ExpressionStatement);

        ExpressionStatement stmt = (ExpressionStatement) program.body()[0];
        assertTrue(stmt.expression() instanceof BinaryExpression);

        BinaryExpression exp = (BinaryExpression) stmt.expression();
        assertEquals(">", exp.operator());
        assertTrue(exp.left() instanceof Identifier);
        assertTrue(exp.right() instanceof NumericLiteral);

        Identifier left = (Identifier) exp.left();
        NumericLiteral right = (NumericLiteral) exp.right();
        assertEquals("x", left.name());
        assertEquals(10, right.value());
    }

}
