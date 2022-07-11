package org.redstom.language.interpreter.variables;

import lombok.Builder;
import lombok.ToString;

@ToString
public class UnknownVariable extends Variable<Object> {

    @Builder
    protected UnknownVariable(String name, Object value) {
        super(name, value);
    }
}

