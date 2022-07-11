package org.redstom.language.utils;

import lombok.SneakyThrows;
import org.redstom.language.exceptions.SyntaxError;
import org.redstom.language.parser.ast.expression.Expression;
import org.redstom.language.parser.ast.expression.Identifier;

public class ParserUtils {
    @SneakyThrows
    public static Expression checkValidAssignmentTarget(Expression ex) {
        if (ex instanceof Identifier) {
            return ex;
        }

        throw new SyntaxError("Invalid assignment target");
    }
}
