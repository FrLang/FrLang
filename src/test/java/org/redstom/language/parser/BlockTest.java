package org.redstom.language.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redstom.language.parser.ast.Program;
import org.redstom.language.parser.ast.statement.EmptyStatement;
import org.redstom.language.parser.ast.statement.BlockStatement;
import org.redstom.language.parser.ast.expression.ExpressionStatement;
import org.redstom.language.parser.ast.literal.NumericLiteral;
import org.redstom.language.parser.ast.literal.StringLiteral;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BlockTest {

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
    void blockStatement() {
        Program result = parser.parse("""
                {
                    42;
                    ;
                }
                """);

        assertEquals(result.body().length, 1);
        assertTrue(result.body()[0] instanceof BlockStatement);

        BlockStatement block = (BlockStatement) result.body()[0];
        assertEquals(block.body().length, 2);
        assertTrue(block.body()[0] instanceof ExpressionStatement);
        assertTrue(block.body()[1] instanceof EmptyStatement);

        ExpressionStatement stmt = (ExpressionStatement) block.body()[0];
        System.out.println(stmt);
        assertTrue(stmt.expression() instanceof NumericLiteral);

        NumericLiteral literal = (NumericLiteral) stmt.expression();
        assertEquals(42, literal.value());
    }

    @Test
    void emptyBlock() {
        Program result = parser.parse("""
                {
                }
                """);

        assertEquals(result.body().length, 1);
        assertTrue(result.body()[0] instanceof BlockStatement);

        BlockStatement block = (BlockStatement) result.body()[0];
        assertEquals(block.body().length, 0);
    }

    @Test
    void nestedBlocksWithLiterals() {
        Program result = parser.parse("""
                {
                    42;
                    {
                        "hello";
                    }
                }
                """);

        assertEquals(result.body().length, 1);
        assertTrue(result.body()[0] instanceof BlockStatement);

        BlockStatement block = (BlockStatement) result.body()[0];
        assertEquals(block.body().length, 2);
        assertTrue(block.body()[0] instanceof ExpressionStatement);
        assertTrue(block.body()[1] instanceof BlockStatement);

        BlockStatement nestedBlock = (BlockStatement) block.body()[1];
        assertEquals(nestedBlock.body().length, 1);
        assertTrue(nestedBlock.body()[0] instanceof ExpressionStatement);

        ExpressionStatement stmt = (ExpressionStatement) block.body()[0];
        assertTrue(stmt.expression() instanceof NumericLiteral);

        NumericLiteral literal = (NumericLiteral) stmt.expression();
        assertEquals(42, literal.value());

        ExpressionStatement stmt2 = (ExpressionStatement) nestedBlock.body()[0];
        assertTrue(stmt2.expression() instanceof StringLiteral);

        StringLiteral literal2 = (StringLiteral) stmt2.expression();
        assertEquals("hello", literal2.value());
    }

}
