package org.redstom.language.interpreter.variables;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Variable<T> {

    public static final Variable<Void> NULL = new Variable<>("null", null);

    private String name;
    private T value;

}
