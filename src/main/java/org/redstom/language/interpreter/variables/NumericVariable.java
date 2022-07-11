package org.redstom.language.interpreter.variables;

import lombok.Builder;
import lombok.ToString;

@ToString
public class NumericVariable extends Variable<Double> {

    @Builder
    protected NumericVariable(String name, Double value) {
        super(name, value);
    }
}
