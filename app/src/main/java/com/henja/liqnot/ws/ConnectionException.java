package com.henja.liqnot.ws;

/**
 * Created by henry on 15/01/2017.
 */

public class ConnectionException extends Exception {

    public ConnectionException(String message) {
        super(message);
    }

    public ConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
