package com.ivi.hybrid.core.exception;

/**
 * author: Rea.X
 * date: 2017/10/25.
 */

public class BridgeException extends RuntimeException{

    public BridgeException() {
    }

    public BridgeException(String message) {
        super(message);
    }

    public BridgeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BridgeException(Throwable cause) {
        super(cause);
    }
}
