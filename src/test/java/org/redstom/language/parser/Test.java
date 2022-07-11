package org.redstom.language.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.val;
import org.redstom.language.parser.ast.ASTNode;
import org.redstom.language.parser.ast.expression.BinaryExpression;
import org.redstom.language.parser.ast.expression.Expression;
import org.redstom.language.parser.ast.expression.ExpressionStatement;
import org.redstom.language.parser.ast.expression.UnaryExpression;
import org.redstom.language.parser.ast.literal.NumericLiteral;
import org.redstom.language.parser.ast.literal.StringLiteral;
import org.redstom.language.parser.ast.statement.BlockStatement;

public class Test {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapterFactory(Serializer.of(Object.class, "type"))
            .create();


    public static void main(String[] args) {
        Parser parser = new Parser();
        val test = parser.parse("""
                var c;
                
                si a {
                    c = 5;
                } sinon si b {
                    c = 9;
                } sinon {
                    c = 13;
                }
                
                c;
                """);

        System.out.println(GSON.toJson(test));
    }

    private static void visit(ASTNode[] nodes) {
        for (ASTNode node : nodes) {
            if (node instanceof BlockStatement stmt) {
                visit(stmt.body());
            } else if (node instanceof ExpressionStatement stmt) {
                eval(stmt);
            }
        }
    }

    private static void eval(ExpressionStatement node) {
        System.out.println(switch (node.expression()) {
            case BinaryExpression be -> eval(be);
            case NumericLiteral nl -> nl.value();
            case StringLiteral sl -> "\"" + sl.value() + "\"";
            default -> "ERROR !";
        });
    }

    private static double eval(BinaryExpression be) {
        double left = calc(be.left()), right = calc(be.right()), result;

        switch (be.operator()) {
            case "+" -> result = left + right;
            case "-" -> result = left - right;
            case "*" -> result = left * right;
            case "/" -> result = left / right;
            default -> throw new IllegalArgumentException("Unsupported operator");
        }

        return result;
    }

    private static double eval(UnaryExpression ue) {
        double value = calc(ue.argument());
        switch (ue.operator()) {
            case "-" -> value = -value;
        }
        return value;
    }

    private static double calc(Expression e) {
        return switch (e) {
            case NumericLiteral nr -> nr.value();
            case BinaryExpression br -> eval(br);
            case UnaryExpression ur -> eval(ur);
            default -> throw new IllegalArgumentException("Unsupported expression");
        };
    }
}
