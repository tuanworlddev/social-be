package org.tuandev.socialbe.exceptions;

public class InsufficientQuantityException extends  RuntimeException {
    public InsufficientQuantityException(String message) {
        super(message);
    }
}
