package org.redstom.language.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.redstom.language.parser.ast.Program;
import org.redstom.language.parser.ast.expression.BinaryExpression;
import org.redstom.language.parser.ast.expression.ExpressionStatement;
import org.redstom.language.parser.ast.literal.NumericLiteral;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MathTest {

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

        assertEquals(1, result.body().length);
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

    @ParameterizedTest
    @CsvSource({
            "1 + 2 + 3;, 1, +, 2, +, 3",
            "1 + 2 * 3;, 1, +, 2, *, 3",
            "1 * 2 + 3;, 1, *, 2, +, 3",
            "1 * 2 * 3;, 1, *, 2, *, 3",
    })
    void testOperations(
            String test,
            int i1,
            String op1,
            int i2,
            String op2,
            int i3
    ) {
        System.out.printf("%s (%s)%n", test, i1 + op1 + i2 + op2 + i3);
        Program result = parser.parse(test);

        System.out.println(result);

        assertEquals(1, result.body().length);
        assertTrue(result.body()[0] instanceof ExpressionStatement);

        ExpressionStatement stmt = (ExpressionStatement) result.body()[0];
        assertTrue(stmt.expression() instanceof BinaryExpression);

        BinaryExpression expr = (BinaryExpression) stmt.expression();
        assertEquals(op2, expr.operator());
        assertTrue(expr.left() instanceof BinaryExpression);
        assertTrue(expr.right() instanceof NumericLiteral);

        BinaryExpression left = (BinaryExpression) expr.left();
        NumericLiteral right = (NumericLiteral) expr.right();
        assertEquals(op1, left.operator());
        assertTrue(left.left() instanceof NumericLiteral);
        assertTrue(left.right() instanceof NumericLiteral);
        assertEquals(i1, ((NumericLiteral) left.left()).value());
        assertEquals(i2, ((NumericLiteral) left.right()).value());
        assertEquals(i3, right.value());
    }

    @Test
    void testMultiplication() {
        Program result = parser.parse("""
                1 * 2;
                """);

        assertEquals(1, result.body().length);
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
    void testParenthesisedAdditionThenMultiplication() {
        Program result = parser.parse("""
                (1 + 2) * 3;
                """);

        assertEquals(1, result.body().length);
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
}
