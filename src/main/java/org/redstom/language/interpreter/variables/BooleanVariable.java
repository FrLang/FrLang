package org.redstom.language.interpreter.variables;

import lombok.Builder;
import lombok.ToString;

@ToString
public class BooleanVariable extends Variable<Boolean> {

    @Builder
    protected BooleanVariable(String name, Boolean value) {
        super(name, value);
    }

}
