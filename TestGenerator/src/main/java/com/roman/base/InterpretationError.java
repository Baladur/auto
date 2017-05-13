package com.roman.base;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by roman on 23.04.2017.
 */
@Setter
@Getter
public class InterpretationError {
    private String message;
    private int line = -1;
    private int column;

    public InterpretationError(String message, int line, int column) {
        this.message = message;
        this.line = line;
        this.column = column;
    }

    public InterpretationError(String message, int column) {
        this.column = column;
    }

    public String toString() {
        return String.format("Error (%d, %d). %s", line, column, message);
    }
}
