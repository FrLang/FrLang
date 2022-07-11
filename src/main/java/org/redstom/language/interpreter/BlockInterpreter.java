package org.redstom.language.interpreter;

import org.redstom.language.interpreter.variables.Variable;
import org.redstom.language.parser.ast.ASTNode;
import org.redstom.language.parser.ast.Program;
import org.redstom.language.parser.ast.expression.AssignmentExpression;
import org.redstom.language.parser.ast.expression.Expression;
import org.redstom.language.parser.ast.expression.Identifier;
import org.redstom.language.parser.ast.literal.*;
import org.redstom.language.parser.ast.statement.BlockStatement;
import org.redstom.language.parser.ast.statement.Statement;

public class BlockInterpreter {

    private Scope scope;

    public BlockInterpreter(Scope scope) {
        this.scope = new Scope(scope);
    }

    public BlockInterpreter() {
        this(null);
    }

    public void execute(BlockStatement block) {
        execute(block.body());
    }

    public void execute(Program program) {
        execute(program.body());
    }

    private void execute(ASTNode[] nodes) {
        for (ASTNode node : nodes) {
            switch (node) {
                case Literal literal -> {
                    System.out.println(this.Literal(literal));
                }
                case Expression expression -> {
                    this.Expression(expression);
                }
                case Statement statement -> {
                    this.Statement(statement);
                }
                default -> {
                }
            }
        }
    }

    private String Literal(Literal literal) {
        return switch (literal) {
            case NumericLiteral lit -> String.valueOf(lit.value());
            case StringLiteral lit -> lit.value();
            case BooleanLiteral lit -> String.valueOf(lit.value());
            case NullLiteral lit -> "nul";
            default -> throw new Error("Unsupported literal type : " + literal.getClass().getSimpleName());
        };
    }

    private Variable<?> Expression(Expression expression) {
        switch (expression) {
            case Identifier id -> System.out.println(scope.get(id.name()));
            case AssignmentExpression assignment -> Assignment(assignment);
            default -> {
            }
        }
        return null;
    }

    private void Assignment(AssignmentExpression assignment) {
        if(!(assignment.left() instanceof Identifier)) {
            throw new Error("Left side of assignment must be an identifier");
        }

        Identifier id = (Identifier) assignment.left();
        //Expression value = Expression(assignment.right());
    }

    private void Statement(Statement statement) {

    }


}
