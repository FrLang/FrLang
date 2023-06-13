package org.redstom.language.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redstom.language.parser.ast.Program;
import org.redstom.language.parser.ast.expression.AssignmentExpression;
import org.redstom.language.parser.ast.expression.BinaryExpression;
import org.redstom.language.parser.ast.expression.ExpressionStatement;
import org.redstom.language.parser.ast.expression.Identifier;
import org.redstom.language.parser.ast.literal.BooleanLiteral;
import org.redstom.language.parser.ast.literal.NumericLiteral;
import org.redstom.language.parser.ast.statement.BlockStatement;
import org.redstom.language.parser.ast.statement.EmptyStatement;
import org.redstom.language.parser.ast.statement.VariableDeclaration;
import org.redstom.language.parser.ast.statement.VariableStatement;
import org.redstom.language.parser.ast.statement.iteration.DoWhileStatement;
import org.redstom.language.parser.ast.statement.iteration.ForStatement.ForIntStatement;
import org.redstom.language.parser.ast.statement.iteration.ForStatement.ForRangeStatement;
import org.redstom.language.parser.ast.statement.iteration.WhileStatement;

import static org.junit.jupiter.api.Assertions.*;

class IterationTest {

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
    void emptyWhileLoop() {
        Program program = parser.parse("""
                tant que vrai;
                """);

        assertEquals(1, program.body().length);
        assertTrue(program.body()[0] instanceof WhileStatement);

        WhileStatement whileStatement = (WhileStatement) program.body()[0];

        assertTrue(whileStatement.test() instanceof BooleanLiteral);
        BooleanLiteral booleanLiteral = (BooleanLiteral) whileStatement.test();
        assertTrue(booleanLiteral.value());

        assertTrue(whileStatement.body() instanceof EmptyStatement);
    }

    @Test
    void filledWhileLoop() {
        Program program = parser.parse("""
                tant que vrai {
                    a;
                }
                """);

        assertEquals(1, program.body().length);
        assertTrue(program.body()[0] instanceof WhileStatement);

        WhileStatement whileStatement = (WhileStatement) program.body()[0];

        assertTrue(whileStatement.test() instanceof BooleanLiteral);
        BooleanLiteral booleanLiteral = (BooleanLiteral) whileStatement.test();
        assertTrue(booleanLiteral.value());

        assertTrue(whileStatement.body() instanceof BlockStatement);
        BlockStatement blockStatement = (BlockStatement) whileStatement.body();
        assertEquals(1, blockStatement.body().length);

        assertTrue(blockStatement.body()[0] instanceof ExpressionStatement);
        ExpressionStatement expressionStatement = (ExpressionStatement) blockStatement.body()[0];

        assertTrue(expressionStatement.expression() instanceof Identifier);
        Identifier identifier = (Identifier) expressionStatement.expression();
        assertEquals("a", identifier.name());
    }


    @Test
    void doWhile() {
        Program program = parser.parse("""
                faire {
                    a;
                } tant que vrai;
                """);

        assertEquals(1, program.body().length);
        assertTrue(program.body()[0] instanceof DoWhileStatement);

        DoWhileStatement doWhileStatement = (DoWhileStatement) program.body()[0];

        assertTrue(doWhileStatement.test() instanceof BooleanLiteral);
        BooleanLiteral booleanLiteral = (BooleanLiteral) doWhileStatement.test();
        assertTrue(booleanLiteral.value());

        assertTrue(doWhileStatement.body() instanceof BlockStatement);
        BlockStatement blockStatement = (BlockStatement) doWhileStatement.body();
        assertEquals(1, blockStatement.body().length);

        assertTrue(blockStatement.body()[0] instanceof ExpressionStatement);
        ExpressionStatement expressionStatement = (ExpressionStatement) blockStatement.body()[0];

        assertTrue(expressionStatement.expression() instanceof Identifier);
        Identifier identifier = (Identifier) expressionStatement.expression();
        assertEquals("a", identifier.name());
    }

    @Test
    void forRangeLoop() {
        Program program = parser.parse("""
                pour i = 0 -> 10 {}
                """);

        assertEquals(1, program.body().length);
        assertTrue(program.body()[0] instanceof ForRangeStatement);

        ForRangeStatement forStatement = (ForRangeStatement) program.body()[0];

        ForRangeStatement.Init init = forStatement.init();
        assertEquals("i", init.identifier().name());

        assertTrue(init.from() instanceof NumericLiteral);
        NumericLiteral numericLiteral = (NumericLiteral) init.from();
        assertEquals(0, numericLiteral.value());

        assertTrue(init.to() instanceof NumericLiteral);
        numericLiteral = (NumericLiteral) init.to();
        assertEquals(10, numericLiteral.value());

        assertNull(init.step());

        assertTrue(forStatement.body() instanceof BlockStatement);
    }

    @Test
    void forRangeLoopWithStep() {
        Program program = parser.parse("""
                pour i = 0 -> 10 (2) {}
                """);

        assertEquals(1, program.body().length);
        assertTrue(program.body()[0] instanceof ForRangeStatement);

        ForRangeStatement forStatement = (ForRangeStatement) program.body()[0];

        ForRangeStatement.Init init = forStatement.init();
        assertEquals("i", init.identifier().name());

        assertTrue(init.from() instanceof NumericLiteral);
        NumericLiteral numericLiteral = (NumericLiteral) init.from();
        assertEquals(0, numericLiteral.value());

        assertTrue(init.to() instanceof NumericLiteral);
        numericLiteral = (NumericLiteral) init.to();
        assertEquals(10, numericLiteral.value());

        assertTrue(init.step() instanceof NumericLiteral);
        numericLiteral = (NumericLiteral) init.step();
        assertEquals(2, numericLiteral.value());

        assertTrue(forStatement.body() instanceof BlockStatement);
    }

    @Test
    void forIntLoop() {
        Program program = parser.parse("""
                pour var i = 0; i < 5; i += 1 {}
                """);

        assertEquals(1, program.body().length);
        assertTrue(program.body()[0] instanceof ForIntStatement);

        ForIntStatement forStatement = (ForIntStatement) program.body()[0];

        VariableStatement init = forStatement.init();
        assertEquals(1, init.declarations().length);

        VariableDeclaration declaration = init.declarations()[0];
        assertEquals("i", declaration.identifier().name());
        assertTrue(declaration.initializer() instanceof NumericLiteral);
        NumericLiteral numericLiteral = (NumericLiteral) declaration.initializer();
        assertEquals(0, numericLiteral.value());

        assertTrue(forStatement.test() instanceof BinaryExpression);
        BinaryExpression binaryExpression = (BinaryExpression) forStatement.test();
        assertTrue(binaryExpression.left() instanceof Identifier);
        Identifier identifier = (Identifier) binaryExpression.left();
        assertEquals("i", identifier.name());

        assertTrue(binaryExpression.right() instanceof NumericLiteral);
        numericLiteral = (NumericLiteral) binaryExpression.right();
        assertEquals(5, numericLiteral.value());

        assertTrue(forStatement.update() instanceof AssignmentExpression);
        AssignmentExpression assignmentExpression = (AssignmentExpression) forStatement.update();
        assertTrue(assignmentExpression.left() instanceof Identifier);
        identifier = (Identifier) assignmentExpression.left();
        assertEquals("i", identifier.name());

        assertEquals("+=", assignmentExpression.operator());

        assertTrue(assignmentExpression.right() instanceof NumericLiteral);
        numericLiteral = (NumericLiteral) assignmentExpression.right();
        assertEquals(1, numericLiteral.value());

        assertTrue(forStatement.body() instanceof BlockStatement);
        BlockStatement blockStatement = (BlockStatement) forStatement.body();
        assertEquals(0, blockStatement.body().length);
    }
}
