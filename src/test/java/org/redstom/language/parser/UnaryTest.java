package org.redstom.language.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redstom.language.parser.ast.Program;
import org.redstom.language.parser.ast.expression.ExpressionStatement;
import org.redstom.language.parser.ast.expression.UnaryExpression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UnaryTest {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapterFactory(Serializer.of(Object.class, "type"))
            .serializeNulls()
            .create();

    private Parser parser;

    @BeforeEach
    public void setUp() {
        this.parser = new Parser();
    }

    @Test
    void testUnaryNot() {
        Program program = parser.parse("""
                !a;
                """);

        assertEquals(1, program.body().length);
        assertTrue(program.body()[0] instanceof ExpressionStatement);

        ExpressionStatement stmt = (ExpressionStatement) program.body()[0];
        assertTrue(stmt.expression() instanceof UnaryExpression);
    }

}
