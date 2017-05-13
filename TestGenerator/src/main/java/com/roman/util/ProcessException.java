package com.roman.util;

/**
 * Created by roman on 11.09.2016.
 */
public class ProcessException extends Exception {
    public ProcessException(String errorMessage) {
        super(errorMessage);
    }

    public ProcessException(InputTestReader reader, String errorMessage) {
        super(String.format("%s: %s", MessageFormatter.errorLine(reader), errorMessage));
    }
}
