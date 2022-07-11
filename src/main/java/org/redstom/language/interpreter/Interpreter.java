package org.redstom.language.interpreter;

import org.redstom.language.parser.ast.Program;

public class Interpreter {

    public void execute(Program program) {
        BlockInterpreter interpreter = new BlockInterpreter();
        interpreter.execute(program);
    }
}
