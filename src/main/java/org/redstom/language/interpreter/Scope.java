package org.redstom.language.interpreter;

import lombok.SneakyThrows;
import org.redstom.language.interpreter.variables.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Scope {

    private final Map<String, Variable<?>> variables = new HashMap<>();
    private final Optional<Scope> parent;

    public Scope(Scope parent) {
        this.parent = Optional.of(parent);
    }

    public Scope() {
        this.parent = Optional.empty();
    }

    @SneakyThrows
    public Variable<?> get(String name) {
        if (variables.containsKey(name)) {
            return variables.get(name);
        }
        if (parent.isPresent()) {
            return parent.get().get(name);
        }

        return Variable.NULL;
    }

    public void set(String name, Object value) {
        variables.put(name, switch (value) {
            case null -> Variable.NULL;
            case String str -> StringVariable.builder()
                    .name(name)
                    .value(str)
                    .build();
            case Double d -> NumericVariable.builder()
                    .name(name)
                    .value(d)
                    .build();
            case Boolean b -> BooleanVariable.builder()
                    .name(name)
                    .value(b)
                    .build();
            default -> UnknownVariable.builder()
                    .name(name)
                    .value(value)
                    .build();
        });
    }
}
