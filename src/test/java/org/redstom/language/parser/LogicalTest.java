package org.redstom.language.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redstom.language.parser.ast.Program;
import org.redstom.language.parser.ast.expression.BinaryExpression;
import org.redstom.language.parser.ast.expression.ExpressionStatement;
import org.redstom.language.parser.ast.expression.Identifier;
import org.redstom.language.parser.ast.expression.LogicalExpression;
import org.redstom.language.parser.ast.literal.NumericLiteral;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LogicalTest {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapterFactory(Serializer.of(Object.class, "type"))
            .serializeNulls()
            .create();

    private Parser parser;

    @BeforeEach
    void setUp() {
        parser = new Parser();
    }

    @Test
    void testAnd() {
        Program program = parser.parse("""
                x > 0 et y < 1;
                """);

        assertEquals(1, program.body().length);
        assertTrue(program.body()[0] instanceof ExpressionStatement);

        ExpressionStatement stmt = (ExpressionStatement) program.body()[0];
        assertTrue(stmt.expression() instanceof LogicalExpression);

        LogicalExpression logicalExpression = (LogicalExpression) stmt.expression();
        assertEquals("et", logicalExpression.operator());

        assertTrue(logicalExpression.left() instanceof BinaryExpression);
        BinaryExpression left = (BinaryExpression) logicalExpression.left();
        assertTrue(left.left() instanceof Identifier);
        assertTrue(left.right() instanceof NumericLiteral);

        Identifier identifier = (Identifier) left.left();
        assertEquals("x", identifier.name());

        NumericLiteral numericLiteral = (NumericLiteral) left.right();
        assertEquals(0, numericLiteral.value());

        assertTrue(logicalExpression.right() instanceof BinaryExpression);
        BinaryExpression right = (BinaryExpression) logicalExpression.right();
        assertTrue(right.left() instanceof Identifier);

        identifier = (Identifier) right.left();
        assertEquals("y", identifier.name());

        numericLiteral = (NumericLiteral) right.right();
        assertEquals(1, numericLiteral.value());
    }


}
