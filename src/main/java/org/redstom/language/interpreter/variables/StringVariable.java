package org.redstom.language.interpreter.variables;

import lombok.Builder;
import lombok.ToString;

@ToString
public class StringVariable extends Variable<String> {

    @Builder
    protected StringVariable(String name, String value) {
        super(name, value);
    }
}
