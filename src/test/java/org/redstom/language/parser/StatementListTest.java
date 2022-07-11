package org.redstom.language.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redstom.language.parser.ast.ASTNode;
import org.redstom.language.parser.ast.Program;
import org.redstom.language.parser.ast.expression.ExpressionStatement;
import org.redstom.language.parser.ast.literal.NumericLiteral;
import org.redstom.language.parser.ast.literal.StringLiteral;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StatementListTest {

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
    public void testTwoStatements() {
        Program program = parser.parse("""
                "hello";
                42;
                """);

        ASTNode[] stmts = program.body();
        assertEquals(2, stmts.length);

        assertTrue(stmts[0] instanceof ExpressionStatement);
        assertTrue(stmts[1] instanceof ExpressionStatement);

        ExpressionStatement str = (ExpressionStatement) stmts[0];
        assertTrue(str.expression() instanceof StringLiteral);
        StringLiteral l1 = (StringLiteral) str.expression();
        assertEquals("hello", l1.value());

        ExpressionStatement num = (ExpressionStatement) stmts[1];
        assertTrue(num.expression() instanceof NumericLiteral);
        NumericLiteral l2 = (NumericLiteral) num.expression();
        assertEquals(42, l2.value());
    }
}
