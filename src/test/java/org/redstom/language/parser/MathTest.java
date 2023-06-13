package org.redstom.language.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redstom.language.parser.ast.Program;
import org.redstom.language.parser.ast.expression.BinaryExpression;
import org.redstom.language.parser.ast.expression.ExpressionStatement;
import org.redstom.language.parser.ast.literal.NumericLiteral;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MathTest {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapterFactory(Serializer.of(Object.class, "type"))
            .create();

    private Parser parser;

    @BeforeEach
    void setUp() {
        this.parser = new Parser();
    }

    @Test
    void testAddition() {
        System.out.println("------");
        Program result = parser.parse("""
                1 + 2;
                """);

        assertEquals(result.body().length, 1);
        assertTrue(result.body()[0] instanceof ExpressionStatement);

        ExpressionStatement stmt = (ExpressionStatement) result.body()[0];
        assertTrue(stmt.expression() instanceof BinaryExpression);

        BinaryExpression expr = (BinaryExpression) stmt.expression();
        assertEquals("+", expr.operator());
        assertTrue(expr.left() instanceof NumericLiteral);
        assertTrue(expr.right() instanceof NumericLiteral);

        NumericLiteral left = (NumericLiteral) expr.left();
        NumericLiteral right = (NumericLiteral) expr.right();
        assertEquals(1, left.value());
        assertEquals(2, right.value());
    }

    @Test
    void testChainedAddition() {
        Program result = parser.parse("""
                1 + 2 + 3;
                """);

        assertEquals(result.body().length, 1);
        assertTrue(result.body()[0] instanceof ExpressionStatement);

        ExpressionStatement stmt = (ExpressionStatement) result.body()[0];
        assertTrue(stmt.expression() instanceof BinaryExpression);

        BinaryExpression expr = (BinaryExpression) stmt.expression();
        assertEquals("+", expr.operator());
        assertTrue(expr.left() instanceof BinaryExpression);
        assertTrue(expr.right() instanceof NumericLiteral);

        BinaryExpression left = (BinaryExpression) expr.left();
        NumericLiteral right = (NumericLiteral) expr.right();
        assertEquals("+", left.operator());
        assertTrue(left.left() instanceof NumericLiteral);
        assertTrue(left.right() instanceof NumericLiteral);
        assertEquals(1, ((NumericLiteral) left.left()).value());
        assertEquals(2, ((NumericLiteral) left.right()).value());
        assertEquals(3, right.value());
    }

    @Test
    void testMultiplication() {
        Program result = parser.parse("""
                1 * 2;
                """);

        assertEquals(result.body().length, 1);
        assertTrue(result.body()[0] instanceof ExpressionStatement);

        ExpressionStatement stmt = (ExpressionStatement) result.body()[0];
        assertTrue(stmt.expression() instanceof BinaryExpression);

        BinaryExpression expr = (BinaryExpression) stmt.expression();
        assertEquals("*", expr.operator());
        assertTrue(expr.left() instanceof NumericLiteral);
        assertTrue(expr.right() instanceof NumericLiteral);

        NumericLiteral left = (NumericLiteral) expr.left();
        NumericLiteral right = (NumericLiteral) expr.right();
        assertEquals(1, left.value());
        assertEquals(2, right.value());
    }

    @Test
    void testAdditionThenMultiplication() {
        Program result = parser.parse("""
                1 + 2 * 3;
                """);

        assertEquals(result.body().length, 1);
        assertTrue(result.body()[0] instanceof ExpressionStatement);

        ExpressionStatement stmt = (ExpressionStatement) result.body()[0];
        assertTrue(stmt.expression() instanceof BinaryExpression);

        BinaryExpression expr = (BinaryExpression) stmt.expression();
        assertEquals("+", expr.operator());
        assertTrue(expr.left() instanceof NumericLiteral);
        assertTrue(expr.right() instanceof BinaryExpression);

        NumericLiteral left = (NumericLiteral) expr.left();
        BinaryExpression right = (BinaryExpression) expr.right();
        assertEquals("*", right.operator());
        assertTrue(right.left() instanceof NumericLiteral);
        assertTrue(right.right() instanceof NumericLiteral);
        assertEquals(1, left.value());
        assertEquals(2, ((NumericLiteral) right.left()).value());
        assertEquals(3, ((NumericLiteral) right.right()).value());
    }

    @Test
    void testParenthesisedAdditionThenMultiplication() {
        Program result = parser.parse("""
                (1 + 2) * 3;
                """);

        assertEquals(result.body().length, 1);
        assertTrue(result.body()[0] instanceof ExpressionStatement);

        ExpressionStatement stmt = (ExpressionStatement) result.body()[0];
        assertTrue(stmt.expression() instanceof BinaryExpression);

        BinaryExpression expr = (BinaryExpression) stmt.expression();
        assertEquals("*", expr.operator());
        assertTrue(expr.left() instanceof BinaryExpression);
        assertTrue(expr.right() instanceof NumericLiteral);

        BinaryExpression left = (BinaryExpression) expr.left();
        NumericLiteral right = (NumericLiteral) expr.right();
        assertEquals("+", left.operator());
        assertTrue(left.left() instanceof NumericLiteral);
        assertTrue(left.right() instanceof NumericLiteral);
        assertEquals(1, ((NumericLiteral) left.left()).value());
        assertEquals(2, ((NumericLiteral) left.right()).value());
        assertEquals(3, right.value());
    }

    @Test
    void testMultiplicationThenAddition() {
        Program result = parser.parse("""
                1 * 2 + 3;
                """);

        assertEquals(result.body().length, 1);
        assertTrue(result.body()[0] instanceof ExpressionStatement);

        ExpressionStatement stmt = (ExpressionStatement) result.body()[0];
        assertTrue(stmt.expression() instanceof BinaryExpression);

        BinaryExpression expr = (BinaryExpression) stmt.expression();
        assertEquals("+", expr.operator());
        assertTrue(expr.left() instanceof BinaryExpression);
        assertTrue(expr.right() instanceof NumericLiteral);

        BinaryExpression left = (BinaryExpression) expr.left();
        NumericLiteral right = (NumericLiteral) expr.right();
        assertEquals("*", left.operator());
        assertTrue(left.left() instanceof NumericLiteral);
        assertTrue(left.right() instanceof NumericLiteral);
        assertEquals(1, ((NumericLiteral) left.left()).value());
        assertEquals(2, ((NumericLiteral) left.right()).value());
        assertEquals(3, right.value());
    }

    @Test
    void testChainedMultiplication() {
        Program result = parser.parse("""
                1 * 2 * 3;
                """);

        assertEquals(result.body().length, 1);
        assertTrue(result.body()[0] instanceof ExpressionStatement);

        ExpressionStatement stmt = (ExpressionStatement) result.body()[0];
        assertTrue(stmt.expression() instanceof BinaryExpression);

        BinaryExpression expr = (BinaryExpression) stmt.expression();
        assertEquals("*", expr.operator());
        assertTrue(expr.left() instanceof BinaryExpression);
        assertTrue(expr.right() instanceof NumericLiteral);

        BinaryExpression left = (BinaryExpression) expr.left();
        NumericLiteral right = (NumericLiteral) expr.right();
        assertEquals("*", left.operator());
        assertTrue(left.left() instanceof NumericLiteral);
        assertTrue(left.right() instanceof NumericLiteral);
        assertEquals(1, ((NumericLiteral) left.left()).value());
        assertEquals(2, ((NumericLiteral) left.right()).value());
        assertEquals(3, right.value());
    }
}
