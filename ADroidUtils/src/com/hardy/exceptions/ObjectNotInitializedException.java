
package com.hardy.exceptions;

public class ObjectNotInitializedException extends RuntimeException {

    public ObjectNotInitializedException() {}

    public ObjectNotInitializedException(String message) {
        super(message);
    }

    public ObjectNotInitializedException(Class t) {
        super("Object is not initialized before use : " + t.getSimpleName() +
              "\n Have you called initialize(context) before calling getInstance()");
    }

}
