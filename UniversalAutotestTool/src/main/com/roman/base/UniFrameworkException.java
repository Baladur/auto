package com.roman.base;

/**
 * Created by roman on 12.11.2016.
 */
public class UniFrameworkException extends Exception {
    public UniFrameworkException(String msg) {
        super(msg);
    }

    public UniFrameworkException(String msg, Throwable t) {
        super(msg, t);
    }
}
