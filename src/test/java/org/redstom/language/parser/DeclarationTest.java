package org.redstom.language.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redstom.language.parser.ast.Program;
import org.redstom.language.parser.ast.statement.VariableDeclaration;
import org.redstom.language.parser.ast.statement.VariableStatement;
import org.redstom.language.parser.ast.expression.AssignmentExpression;
import org.redstom.language.parser.ast.expression.BinaryExpression;
import org.redstom.language.parser.ast.expression.Identifier;
import org.redstom.language.parser.ast.literal.NumericLiteral;

import static org.junit.jupiter.api.Assertions.*;

class DeclarationTest {

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
    void testSingleEmptyDeclaration() {
        Program program = parser.parse("var x;");

        assertEquals(1, program.body().length);
        assertTrue(program.body()[0] instanceof VariableStatement);

        VariableStatement stmt = (VariableStatement) program.body()[0];
        assertEquals(1, stmt.declarations().length);
        assertNotNull(stmt.declarations()[0]);

        VariableDeclaration declaration = stmt.declarations()[0];
        assertEquals("x", declaration.identifier().name());
        assertNull(declaration.initializer());
    }

    @Test
    void testSingleNumericDeclaration() {
        Program program = parser.parse("var x = 42;");

        assertEquals(1, program.body().length);
        assertTrue(program.body()[0] instanceof VariableStatement);

        VariableStatement stmt = (VariableStatement) program.body()[0];
        assertEquals(1, stmt.declarations().length);
        assertNotNull(stmt.declarations()[0]);

        VariableDeclaration declaration = stmt.declarations()[0];
        assertEquals("x", declaration.identifier().name());

        assertTrue(declaration.initializer() instanceof NumericLiteral);
        NumericLiteral literal = (NumericLiteral) declaration.initializer();
        assertEquals(42, literal.value());
    }

    @Test
    void testSingleCalculingDeclaration() {
        Program program = parser.parse("var x = (42 - 5) * 2;");

        assertEquals(1, program.body().length);
        assertTrue(program.body()[0] instanceof VariableStatement);

        VariableStatement stmt = (VariableStatement) program.body()[0];
        assertEquals(1, stmt.declarations().length);
        assertNotNull(stmt.declarations()[0]);

        VariableDeclaration declaration = stmt.declarations()[0];
        assertEquals("x", declaration.identifier().name());

        assertTrue(declaration.initializer() instanceof BinaryExpression);
        BinaryExpression literal = (BinaryExpression) declaration.initializer();
        assertEquals("*", literal.operator());

        assertTrue(literal.left() instanceof BinaryExpression);
        BinaryExpression left = (BinaryExpression) literal.left();
        assertEquals("-", left.operator());
        assertEquals(42, ((NumericLiteral) left.left()).value());
        assertEquals(5, ((NumericLiteral) left.right()).value());

        assertEquals(2, ((NumericLiteral) literal.right()).value());
    }

    @Test
    void testDoubleEmptyDeclarations() {
        Program program = parser.parse("var x, y;");

        assertEquals(1, program.body().length);
        assertTrue(program.body()[0] instanceof VariableStatement);

        VariableStatement stmt = (VariableStatement) program.body()[0];
        assertEquals(2, stmt.declarations().length);
        assertNotNull(stmt.declarations()[0]);
        assertNotNull(stmt.declarations()[1]);

        VariableDeclaration declaration = stmt.declarations()[0];
        assertEquals("x", declaration.identifier().name());
        assertNull(declaration.initializer());

        declaration = stmt.declarations()[1];
        assertEquals("y", declaration.identifier().name());
        assertNull(declaration.initializer());
    }

    @Test
    void testDoubleDeclarationsWithFirstEmptyAndSecondDeclared() {
        Program program = parser.parse("var x, y = 10;");

        assertEquals(1, program.body().length);
        assertTrue(program.body()[0] instanceof VariableStatement);

        VariableStatement stmt = (VariableStatement) program.body()[0];
        assertEquals(2, stmt.declarations().length);
        assertNotNull(stmt.declarations()[0]);
        assertNotNull(stmt.declarations()[1]);

        VariableDeclaration declaration = stmt.declarations()[0];
        assertEquals("x", declaration.identifier().name());
        assertNull(declaration.initializer());

        declaration = stmt.declarations()[1];
        assertEquals("y", declaration.identifier().name());
        assertTrue(declaration.initializer() instanceof NumericLiteral);
        NumericLiteral literal = (NumericLiteral) declaration.initializer();
        assertEquals(10, literal.value());
    }

    @Test
    void testAssignmentToAlreadyExistingVariableReassigned() {
        Program program = parser.parse("var x = y = 10;");

        assertEquals(1, program.body().length);
        assertTrue(program.body()[0] instanceof VariableStatement);

        VariableStatement stmt = (VariableStatement) program.body()[0];
        assertEquals(1, stmt.declarations().length);

        VariableDeclaration declaration = stmt.declarations()[0];
        assertEquals("x", declaration.identifier().name());
        assertTrue(declaration.initializer() instanceof AssignmentExpression);

        AssignmentExpression assignment = (AssignmentExpression) declaration.initializer();
        assertEquals("=", assignment.operator());
        assertTrue(assignment.left() instanceof Identifier);
        assertEquals("y", ((Identifier) assignment.left()).name());
        assertTrue(assignment.right() instanceof NumericLiteral);
        assertEquals(10, ((NumericLiteral) assignment.right()).value());
    }

}
