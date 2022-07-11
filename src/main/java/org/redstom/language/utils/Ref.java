package org.redstom.language.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Ref<T> {

    public static <T> Ref<T> ref(T value) {
        return new Ref<>(value);
    }

    private T value;

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}
