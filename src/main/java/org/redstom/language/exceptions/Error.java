package org.redstom.language.exceptions;

public class Error extends RuntimeException {

    public Error(String message) {
        super(message);
    }

    public void show() {
        System.err.println(getClass().getSimpleName() + " : " + getMessage());
        System.exit(1);
    }
}
