package org.redstom.language.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redstom.language.parser.ast.Program;
import org.redstom.language.parser.ast.expression.AssignmentExpression;
import org.redstom.language.parser.ast.expression.ExpressionStatement;
import org.redstom.language.parser.ast.expression.Identifier;
import org.redstom.language.parser.ast.literal.NumericLiteral;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AssignmentTest {

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapterFactory(Serializer.of(Object.class, "type"))
            .setPrettyPrinting()
            .create();

    private Parser parser;

    @BeforeEach
    void setUp() {
        this.parser = new Parser();
    }

    @Test
    void testAssignment() {
        Program result = parser.parse("""
                x = 42;
                """);

        assertEquals(1, result.body().length);
        assertTrue(result.body()[0] instanceof ExpressionStatement);

        ExpressionStatement stmt = (ExpressionStatement) result.body()[0];
        assertTrue(stmt.expression() instanceof AssignmentExpression);

        AssignmentExpression expr = (AssignmentExpression) stmt.expression();
        assertEquals("=", expr.operator());
        assertTrue(expr.left() instanceof Identifier);
        assertTrue(expr.right() instanceof NumericLiteral);

        Identifier left = (Identifier) expr.left();
        NumericLiteral right = (NumericLiteral) expr.right();
        assertEquals("x", left.name());
        assertEquals(42, right.value());
    }

    @Test
    void testChainedAssignment() {
        Program result = parser.parse("""
                x = y = 42;
                """);

        assertEquals(1, result.body().length);
        assertTrue(result.body()[0] instanceof ExpressionStatement);

        ExpressionStatement stmt = (ExpressionStatement) result.body()[0];
        assertTrue(stmt.expression() instanceof AssignmentExpression);

        AssignmentExpression expr = (AssignmentExpression) stmt.expression();
        assertEquals("=", expr.operator());
        assertTrue(expr.left() instanceof Identifier);
        assertTrue(expr.right() instanceof AssignmentExpression);

        Identifier left = (Identifier) expr.left();
        AssignmentExpression right = (AssignmentExpression) expr.right();
        assertEquals("x", left.name());
        assertEquals("=", right.operator());
        assertTrue(right.left() instanceof Identifier);
        assertTrue(right.right() instanceof NumericLiteral);

        Identifier left2 = (Identifier) right.left();
        NumericLiteral right2 = (NumericLiteral) right.right();
        assertEquals("y", left2.name());
        assertEquals(42, right2.value());
    }

    @Test
    void testComplexAssignment() {
        Program result = parser.parse("""
                x += 4;
                """);

        assertEquals(1, result.body().length);
        assertTrue(result.body()[0] instanceof ExpressionStatement);

        ExpressionStatement stmt = (ExpressionStatement) result.body()[0];
        assertTrue(stmt.expression() instanceof AssignmentExpression);

        AssignmentExpression expr = (AssignmentExpression) stmt.expression();
        assertEquals("+=", expr.operator());
        assertTrue(expr.left() instanceof Identifier);
        assertTrue(expr.right() instanceof NumericLiteral);

        Identifier left = (Identifier) expr.left();
        NumericLiteral right = (NumericLiteral) expr.right();
        assertEquals("x", left.name());
        assertEquals(4, right.value());
    }
}
